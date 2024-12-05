@file:OptIn(ExperimentalMaterial3Api::class)

package br.gonzaga.logincomposeapp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.gonzaga.logincomposeapp.ui.theme.LoginComposeAppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.common.api.ApiException

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            LoginComposeAppTheme {

                val navController = rememberNavController()


                NavHost(navController = navController, startDestination = "login_screen") {
                    composable("login_screen") {
                        LoginScreen(
                            modifier = Modifier.padding(16.dp),
                            onLogin = { email, password -> performLogin(email, password, navController) },
                            onRegister = { email, password, confirmPassword -> performRegister(email, password, confirmPassword) },
                            onGoogleLogin = { performGoogleLogin(navController) }
                        )
                    }

                    composable("home_screen") {
                        HomeScreen()
                    }
                }
            }
        }
    }

    private fun performLogin(email: String, password: String, navController: NavController) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("Login", "Login bem-sucedido: ${user?.email}")
                    Toast.makeText(this, "Bem-vindo, ${user?.email}!", Toast.LENGTH_SHORT).show()


                    navController.navigate("home_screen")
                } else {
                    Log.e("Login", "Erro ao fazer login", task.exception)
                    Toast.makeText(this, "Erro no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun performRegister(email: String, password: String, confirmPassword: String) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("Registro", "Usuário criado: ${user?.email}")
                    Toast.makeText(this, "Conta criada com sucesso para ${user?.email}!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("Registro", "Erro ao criar conta", task.exception)
                    Toast.makeText(this, "Erro ao criar conta: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun performGoogleLogin(navController: NavController) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        navController.navigate("home_screen")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken!!)
            } catch (e: ApiException) {
                Log.w("GoogleLogin", "Falha no login com Google", e)
                Toast.makeText(this, "Erro no login com Google", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("GoogleLogin", "Login bem-sucedido: ${user?.email}")
                    Toast.makeText(this, "Bem-vindo, ${user?.email}!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("GoogleLogin", "Erro ao autenticar com o Google", task.exception)
                    Toast.makeText(this, "Erro ao autenticar com Google: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String) -> Unit,
    onGoogleLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF7F00FF), // Roxo
                        Color(0xFFE100FF)  // Rosa
                    )
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Text(
                text = "LOGIN",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Email Input
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.White, // Cor do texto quando em foco
                    unfocusedTextColor = Color.White, // Cor do texto quando não em foco
                    containerColor = Color.Transparent // Cor do fundo
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.White, // Cor do texto quando em foco
                    unfocusedTextColor = Color.White, // Cor do texto quando não em foco
                    containerColor = Color.Transparent // Cor do fundo
                )
            )

            if (isRegisterMode) {
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = Color.White, // Cor do texto quando em foco
                        unfocusedTextColor = Color.White, // Cor do texto quando não em foco
                        containerColor = Color.Transparent // Cor do fundo
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login/Register Button
            Button(
                onClick = {
                    if (isRegisterMode) {
                        onRegister(email, password, confirmPassword)
                    } else {
                        onLogin(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF7F00FF)
                )
            ) {
                Text(text = if (isRegisterMode) "Registrar" else "Entrar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Google Login
            TextButton(onClick = onGoogleLogin) {
                Text(text = "Ou faça login com o Google", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Switch to Register mode
            TextButton(onClick = { isRegisterMode = !isRegisterMode }) {
                Text(text = if (isRegisterMode) "Já tem uma conta? Entrar" else "Ainda não tem uma conta? Registrar", color = Color.White)
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7F00FF)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Bem-vindo à tela principal!",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginComposeAppTheme {
        LoginScreen(
            onLogin = { _, _ -> },
            onRegister = { _, _, _ -> },
            onGoogleLogin = {}
        )
    }
}
