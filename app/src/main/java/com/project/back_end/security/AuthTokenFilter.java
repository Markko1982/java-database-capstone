package com.project.back_end.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.back_end.services.Service;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final AntPathMatcher matcher = new AntPathMatcher();

    private final Service service;
    private final ObjectMapper objectMapper;
    private final String apiPath; // ex: "/api/"

    private static class Rule {
        final String httpMethod;
        final String pattern;
        final String roleOrVar; // ex: "doctor", "patient", "admin" ou "@user" (vem da rota)

        Rule(String httpMethod, String pattern, String roleOrVar) {
            this.httpMethod = httpMethod;
            this.pattern = pattern;
            this.roleOrVar = roleOrVar;
        }
    }

    // Rotas públicas (não exigem token)
    private final List<Rule> publicRules;

    // Rotas protegidas (exigem Bearer + validação de role)
    private final List<Rule> protectedRules;

    public AuthTokenFilter(
            Service service,
            ObjectMapper objectMapper,
            @Value("${api.path:}") String apiPath) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.apiPath = normalizeApiPath(apiPath);

        String doctorBase = this.apiPath + "doctor";
        String prescriptionBase = this.apiPath + "prescription";
        String adminBase = this.apiPath + "admin";
        String patientBase = this.apiPath + "patient";
        String appointmentsBase = this.apiPath + "appointments";

        // ajuste aqui conforme suas rotas reais
        this.publicRules = List.of(
                new Rule("POST", patientBase, null),
                new Rule("POST", patientBase + "/login", null),

                new Rule("GET", doctorBase, null),
                new Rule("GET", doctorBase + "/filter/**", null),
                new Rule("POST", doctorBase + "/login", null),
                new Rule("POST", adminBase + "/login", null));

        this.protectedRules = List.of(
                // appointments (Bearer)
                new Rule("GET", appointmentsBase + "/{date}/{patientName}", "doctor"),
                new Rule("POST", appointmentsBase, "patient"),
                new Rule("PUT", appointmentsBase, "patient"),
                new Rule("DELETE", appointmentsBase + "/{id}", "patient"),

                // patient (Bearer)
                new Rule("GET", patientBase, "patient"),
                new Rule("GET", patientBase + "/{id}/appointments", "patient"),
                new Rule("GET", patientBase + "/appointments", "patient"),
                new Rule("GET", patientBase + "/filter/{condition}/{name}", "patient"),

                // doctor (Bearer)
                new Rule("GET", doctorBase + "/availability/{user}/{doctorId}/{date}", "@user"),
                new Rule("POST", doctorBase, "admin"),
                new Rule("PUT", doctorBase, "admin"),
                new Rule("DELETE", doctorBase + "/{id}", "admin"),

                // prescription (Bearer)
                new Rule("POST", prescriptionBase, "doctor"),
                new Rule("GET", prescriptionBase + "/{appointmentId}", "doctor"));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();

        // Preflight CORS
        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Se for público, segue
        if (matchesAny(publicRules, method, path)) {
            filterChain.doFilter(request, response);
            return;
        }

        Rule protectedRule = firstMatch(protectedRules, method, path);
        if (protectedRule == null) {
            // não mapeado como protegido → deixa passar (ou você pode bloquear por padrão)
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractBearerToken(request.getHeader("Authorization"));
        if (token == null) {
            writeJson(response, HttpServletResponse.SC_UNAUTHORIZED,
                    Map.of("message", "Token ausente ou inválido. Use Authorization: Bearer <token>."));
            return;
        }

        String requiredRole = resolveRole(protectedRule, path);
        if (requiredRole != null) {
            ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, requiredRole);
            if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
                int status = tokenCheck.getStatusCodeValue();
                Map<String, String> body = tokenCheck.getBody() != null
                        ? tokenCheck.getBody()
                        : Map.of("message", "Não autorizado.");
                writeJson(response, status, body);
                return;
            }
        }

        // token válido → disponibiliza para o controller/service
        request.setAttribute("token", token);

        filterChain.doFilter(request, response);
    }

    private boolean matchesAny(List<Rule> rules, String method, String path) {
        return rules.stream().anyMatch(r -> r.httpMethod.equalsIgnoreCase(method) && matcher.match(r.pattern, path));
    }

    private Rule firstMatch(List<Rule> rules, String method, String path) {
        return rules.stream()
                .filter(r -> r.httpMethod.equalsIgnoreCase(method) && matcher.match(r.pattern, path))
                .findFirst()
                .orElse(null);
    }

    private String resolveRole(Rule rule, String path) {
        if (rule.roleOrVar == null)
            return null;
        if (!rule.roleOrVar.startsWith("@"))
            return rule.roleOrVar;

        // role dinâmica vinda de variável de rota, ex "@user"
        String varName = rule.roleOrVar.substring(1);
        Map<String, String> vars = matcher.extractUriTemplateVariables(rule.pattern, path);
        return vars.get(varName);
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null)
            return null;

        // aceita "Bearer " com qualquer casing
        if (authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String token = authorization.substring(7).trim();
            return token.isEmpty() ? null : token;
        }
        return null;
    }

    private void writeJson(HttpServletResponse response, int status, Map<String, ?> body) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private String normalizeApiPath(String p) {
        if (p == null)
            return "/";
        String r = p.trim();
        if (r.isEmpty())
            return "/";
        if (!r.startsWith("/"))
            r = "/" + r;
        if (!r.endsWith("/"))
            r = r + "/";
        return r;
    }
}