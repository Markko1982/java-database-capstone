# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

História de Usuário 1: Login Seguro
Título: Como um administrador, eu quero fazer login no portal com meu nome de usuário e senha para que eu possa gerenciar a plataforma com segurança.

Critérios de Aceitação:

Dado que estou na página de login, quando insiro meu nome de usuário e senha corretos e clico em "Entrar", devo ser redirecionado para o painel de administração.

Dado que estou na página de login, quando insiro credenciais inválidas, devo ver uma mensagem de erro como "Usuário ou senha inválidos".

Após o login, meu nível de acesso deve ser de administrador, me dando permissão para ver todas as funcionalidades de gestão.

Prioridade: Alta

Story Points: [A ser definido pela equipe]

Notes: [A ser definido pela equipe]

História de Usuário 2: Logout de Segurança
Título: Como um administrador, eu quero fazer logout (sair) do portal para que eu possa proteger o sistema contra acesso não autorizado.

Critérios de Aceitação:

Dado que estou logado no sistema, ao clicar no botão "Sair", minha sessão deve ser encerrada.

Após o logout, devo ser redirecionado para a página de login pública.

Após sair, não devo conseguir acessar nenhuma página do painel de administração usando o botão "Voltar" do navegador.

Prioridade: Alta

Story Points: [A ser definido pela equipe]

Notes: [A ser definido pela equipe]

História de Usuário 3: Adicionar Médico
Título: Como um administrador, eu quero adicionar novos médicos ao portal para que a lista de profissionais disponíveis para os usuários esteja sempre atualizada.

Critérios de Aceitação:

Dado que estou no painel de administração, ao acessar a seção "Médicos", devo ver um botão "Adicionar Novo Médico".

Ao clicar no botão, um formulário deve ser exibido para que eu possa inserir as informações do médico (ex: nome, CRM, especialidade).

Ao preencher os dados válidos e salvar, o novo médico deve aparecer na lista de médicos do portal e eu devo ver uma mensagem de sucesso.

Se eu tentar salvar com campos obrigatórios em branco, o sistema deve me notificar com uma mensagem de erro.

Prioridade: Alta

Story Points: [A ser definido pela equipe]

Notes: [A ser definido pela equipe]

História de Usuário 4: Excluir Médico
Título: Como um administrador, eu quero excluir o perfil de um médico do portal para que eu possa remover profissionais que não fazem mais parte da plataforma.

Critérios de Aceitação:

Dado que estou na lista de médicos, cada médico deve ter uma opção para "Excluir".

Ao clicar em "Excluir", uma mensagem de confirmação deve aparecer para evitar exclusão acidental (ex: "Tem certeza de que deseja excluir este médico?").

Após confirmar a exclusão, o perfil do médico deve ser removido do sistema e não ser mais visível para os usuários.

Prioridade: Média

Story Points: [A ser definido pela equipe]

Notes: [A ser definido pela equipe]

História de Usuário 5: Visualizar Estatísticas
Título: Como um administrador, eu quero visualizar as estatísticas de uso, como o número de consultas por mês, para que eu possa acompanhar o desempenho e a utilização da plataforma.

Critérios de Aceitação:

Dado que estou no painel de administração, deve existir uma seção de "Relatórios" ou "Estatísticas".

Ao acessar essa seção, devo conseguir ver um gráfico ou tabela com o número total de consultas por mês.

Devo ter a opção de filtrar os dados por um período de tempo (ex: últimos 6 meses, ano atual).

Prioridade: Média

Story Points: [A ser definido pela equipe]

Notes: Esta história representa a necessidade do usuário. A implementação técnica (executar um stored procedure no MySQL) é um detalhe de como a história será construída, mas não precisa estar no título da história.
