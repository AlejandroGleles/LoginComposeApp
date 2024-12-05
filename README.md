# 📱 Login Compose App

Este é um aplicativo de login desenvolvido em **Kotlin** no **Android Studio** usando **Jetpack Compose** e **Firebase**. O aplicativo oferece funcionalidades de autenticação com **e-mail/senha** e **login com Google**. Após o login, o usuário é redirecionado para a tela principal do aplicativo.

🧩 **Funcionalidades**
- ✅ Login com e-mail e senha
- ✅ Registro de novos usuários
- ✅ Login com Google usando Firebase Authentication
- 🚀 Design moderno utilizando **Jetpack Compose**
- 🎨 Interface de usuário com fundo gradiente

🚀 **Como Utilizar**
1. **Clonar o Repositório**  
   Faça o clone do repositório para o seu ambiente local:

   git clone https://github.com/seu-usuario/LoginComposeApp.git
Configurar o Ambiente
Abra o projeto no Android Studio. Certifique-se de que todas as dependências do arquivo build.gradle foram sincronizadas.

Configurar o Firebase

Acesse o console do Firebase e crie um novo projeto.
Ative a autenticação com e-mail/senha e Google no Firebase Authentication.
Baixe o arquivo google-services.json e coloque-o no diretório app/ do seu projeto.
Executar o Aplicativo
Conecte um dispositivo Android ou inicie um emulador. Clique em "Run" no Android Studio.

🖥️ Estrutura do Projeto
Principais Diretórios e Arquivos:

br.gonzaga.logincomposeapp

MainActivity.kt: Configuração principal do aplicativo, integração com Firebase e gerenciamento da navegação.
LoginScreen.kt: Tela de login com campos para e-mail, senha e opções de login com Google.
HomeScreen.kt: Tela exibida após o login bem-sucedido, com uma mensagem de boas-vindas.
br.gonzaga.logincomposeapp.ui.theme

LoginComposeAppTheme.kt: Definições de tema e estilo para o aplicativo.
🔧 Tecnologias Utilizadas

Kotlin: Linguagem principal para o desenvolvimento.
Jetpack Compose: Biblioteca moderna para criação de interfaces de usuário.
Firebase Authentication: Autenticação de usuários via e-mail/senha e Google.
Google Sign-In: Implementação de login com Google.
ActivityResultContracts: Gerenciamento do resultado da atividade de login com Google.
📷 Telas do Aplicativo

Tela de Login: Exibe campos para login com e-mail/senha e login com Google.
Tela Inicial: Exibe uma mensagem de boas-vindas após login bem-sucedido.
Licença
Este projeto está licenciado sob a MIT License. Consulte o arquivo LICENSE para mais detalhes.
