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

História de Usuário 1: Explorar Médicos sem Cadastro
Título: Como um visitante, eu quero visualizar a lista de médicos sem fazer login para que eu possa explorar as opções antes de me registrar.

Critérios de Aceitação:

Dado que estou na página inicial do portal (sem login), devo conseguir acessar a lista de médicos.

A lista deve exibir informações básicas de cada médico, como nome, foto e especialidade.

Eu não devo ser forçado a criar uma conta ou fazer login para visualizar esta lista.

Prioridade: Alta

Story Points: [A ser definido pela equipe]

Notes: [A ser definido pela equipe]

História de Usuário 2: Cadastro na Plataforma
Título: Como um novo usuário, eu quero me inscrever usando meu e-mail e senha para que eu possa agendar consultas.

Critérios de Aceitação:

Dado que estou na página de cadastro, devo encontrar campos para inserir nome, e-mail e senha.

Ao preencher os dados corretamente e submeter o formulário, minha conta de paciente deve ser criada.

Após o cadastro, devo ser redirecionado para a página de login ou para o meu painel de paciente já autenticado.

Se o e-mail que eu usar já estiver cadastrado, o sistema deve me informar com uma mensagem de erro clara.

Prioridade: Alta

Story Points: [A ser definido pela equipe]

Notes: [A ser definido pela equipe]

História de Usuário 3: Agendar Consulta
Título: Como um paciente, eu quero agendar uma consulta com um médico em um horário específico para que eu possa receber atendimento.

Critérios de Aceitação:

Dado que estou logado e na página de um médico, devo conseguir ver sua agenda com os horários disponíveis.

Ao selecionar um horário vago, devo ser levado a uma tela de confirmação com os detalhes (médico, data, hora).

Após confirmar, o horário deve ser reservado para mim e ficar indisponível para outros usuários.

Devo receber uma notificação de sucesso confirmando o meu agendamento.

Prioridade: Alta

Story Points: [A ser definido pela equipe]

Notes: Esta história engloba a necessidade de "fazer login e agendar".

História de Usuário 4: Visualizar Consultas Futuras
Título: Como um paciente, eu quero visualizar minhas consultas futuras para que eu possa me preparar adequadamente.

Critérios de Aceitação:

Dado que estou logado no meu painel, deve haver uma seção clara para "Minhas Consultas".

Nesta seção, todas as minhas consultas futuras devem ser listadas em ordem de data.

Cada consulta na lista deve exibir o nome do médico, a data e a hora.

Prioridade: Média

Story Points: [A ser definido pela equipe]

Notes: [A ser definido pela equipe]

História de Usuário 5: Sair do Portal
Título: Como um paciente, eu quero sair do portal (fazer logout) para que eu possa proteger minha conta e meus dados.

Critérios de Aceitação:

Dado que estou logado, devo encontrar um botão ou link de "Sair" em um local visível (ex: no menu do meu perfil).

Ao clicar em "Sair", minha sessão deve ser finalizada imediatamente.

Após sair, devo ser redirecionado para a página inicial ou para a página de login.

Prioridade: Alta

Story Points: [A ser definido pela equipe]

Notes: [A ser definido pela equipe]

Story Points: [A ser definido pela equipe]

Notes: Esta história representa a necessidade do usuário. A implementação técnica (executar um stored procedure no MySQL) é um detalhe de como a história será construída, mas não precisa estar no título da história.
