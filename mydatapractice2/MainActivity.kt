package com.example.mydatapractice2

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.mydatapractice2.data.AppDatabase
import com.example.mydatapractice2.data.User
import com.example.mydatapractice2.ui.theme.MydataPractice2Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MydataPractice2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current as Activity
                    val db = remember {
                        AppDatabase.getDatabase(context)
                    }

                    val userList by db.userDao().getAll().collectAsState(initial = emptyList())


                    val scope = rememberCoroutineScope()
                    var name by remember { mutableStateOf("") }
                    var userName by remember { mutableStateOf("") }
                    var mail by remember { mutableStateOf("") }
                    var userMail by remember { mutableStateOf("") }
                    var phone by remember { mutableStateOf("") }
                    var userPhone by remember { mutableStateOf("") }


                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ){
                        Row(modifier =
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = name,
                                onValueChange = { name = it },
                                label = {
                                    Row(
                                        Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "person"
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text("이름을 입력하세요")
                                    }
                                }
                            )
                        }

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = mail,
                            onValueChange = { mail = it },
                            label = {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.MailOutline,
                                        contentDescription = "email"
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text("메일을 입력하세요")
                                }
                            }
                        )

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = phone,
                            onValueChange = { phone = it },
                            label = {
                                Row(
                                    Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "phone"
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text("연락처를 입력하세요")
                                }
                            }
                        )

//                        Row(
//                            horizontalArrangement = Arrangement.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
                            Button(
                                modifier= Modifier.align(Alignment.CenterHorizontally),

                                onClick = {
                                    val newUser = User(
                                        name = userName,
                                        mail = userMail,
                                        phone = userPhone,

                                        )
                                    scope.launch(Dispatchers.IO) {
                                        db.userDao().insertAll(newUser)
                                    }
                                    userName = name
                                    userMail = mail
                                    userPhone = phone


                                }) {
                                Text(text = "등록")
                            }
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        Text(text = "등록된 이름: $userName")
                        Text(text = "등록된 메일: $userMail")
                        Text(text = "등록된 연락처: $userPhone")
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )

                        Column {
                            for (user in userList) {
                                UserItem(user = user, onDelete = {
                                    scope.launch(Dispatchers.IO) {
                                        db.userDao().delete(user)
                                    }
                                })
                            }
                        }

                    }
                }

            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Column {
        Text(text = "user name: ${user.name}")
        Text(text = "user mail: ${user.mail}")
        Text(text = "user phone: ${user.phone}")
    }
}

@Composable
fun UserItem(user: User, onDelete: () -> Unit) {
    Column {
        Text(text = "사용자 이름: ${user.name}")
        Text(text = "사용자 이메일: ${user.mail}")
        Text(text = "사용자 전화번호: ${user.phone}")


            Button(
                onClick = onDelete,
                modifier = Modifier.padding(8.dp)
                                    .align(Alignment.CenterHorizontally) )
                                    {
                Text(text = "삭제")
            }
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )

        }
    }
