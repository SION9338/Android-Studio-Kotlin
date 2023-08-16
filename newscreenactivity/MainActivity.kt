package com.example.newscreenactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newscreenactivity.ui.theme.NewScreenActivityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewScreenActivityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("details") { DetailsScreen(navController) }
        composable("next_destination/{selectedMenu}") { backStackEntry ->
            val selectedMenu = backStackEntry.arguments?.getString("selectedMenu") ?: ""
            NextScreen(navController, selectedMenu)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting("아래의 버튼을 눌러 시작하세요!")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("details") }
            ) {
                Text("메뉴 추천 시작!")
            }
        }
    }
}

@Composable
fun DetailsScreen(navController: NavController) {
    var selectedMenu by remember { mutableStateOf("") }

    if (selectedMenu.isEmpty()) {
        val menuList = listOf("한식", "양식", "일식", "중식")
        selectedMenu = menuList.random()
    }

    val buttonText = if (selectedMenu.isNotEmpty()) {
        "${selectedMenu}이 뽑혔습니다!"
    } else {
        "뽑혔습니다!"
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting(buttonText, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { navController.navigate("home") }
                ) {
                    Text("뒤로")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        selectedMenu = getRandomMenuExceptSelected(selectedMenu)
                    }
                ) {
                    Text("다시 뽑기")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        navController.navigate("next_destination/$selectedMenu")
                    }
                ) {
                    Text("다음")
                }
            }
        }
    }
}

fun getRandomMenuExceptSelected(selected: String): String {
    val menuList = listOf("한식", "양식", "일식", "중식")
    val remainingMenus = menuList.filter { it != selected }
    return remainingMenus.random()
}

@Composable
fun NextScreen(navController: NavController, selectedMenu: String) {
    var recommendedDish by remember { mutableStateOf("") }

    val koreanDishes = listOf("불고기 덮밥", "된장찌개", "생선조림", "김치찌개", "냉면", "삼계탕", "비빔밥", "제육덮밥", "김치볶음밥", "닭볶음탕", "갈비찜", "장어덮밥",
        "돼지불백", "닭곰탕", "순두부찌개", "생선구이", "메밀전병", "돼지고기 두루치기", "감자탕", "비빔밥", "육개장", "떡만두국", "해물순두부찌개", "순대국밥", "보쌈", "족발",
        "삼겹살", "대패삼겹살", "오리주물럭", "해물뚝배기", "전복죽", "뚝불고기", "육회", "회", "회덮밥", "물회", "돼지갈비", "소갈비", "낙지볶음", "조개찜", "동태찌개", "낙지볶음밥",
        "곱창전골", "장어구이", "치킨", "김치전", "해물파전", "떡볶이", "순대", "김밥", "닭발", "고로케", "두부김치", "튀김", "대하구이", "닭꼬치", "호떡", "칼국수",
        "샤브샤브", "라면", "돈까스", "낙곱새", "소고기")
    val westernDishes = listOf("스테이크 샐러드", "치킨 샐러드", "파스타", "샌드위치", "그라탕", "햄버거", "오믈렛", "피자", "알리오 올리오", "스파게티", "버팔로 윙", "미트볼",
        "새우 스테이크", "포크립", "비프 스튜", "그릴드 치킨", "그릴드 랍스터", "케밥", "치킨 텐더", "감바스", "핫도그", "퀘사디아", "브리또", "카나페", "빵", "라자냐", "스크램블 에그",
        "오므라이스", "도넛")
    val chineseDishes = listOf("자장면", "볶음밥", "자장면", "볶음밥", "탕수육", "깐풍기", "짬뽕", "칠리 새우", "딤섬", "고추잡채", "유린기", "탄탄면", "깐쇼 새우", "깐쇼 치킨",
        "훠궈", "꿔바로우", "양꼬치", "양고기", "마라탕", "마라롱샤", "자장밥", "짬뽕밥", "멘보샤", "마파두부", "마파두부덮밥")
    val japaneseDishes = listOf("초밥", "덴뿌라", "우동", "라멘", "가츠동", "돈부리", "오니기리", "샤브샤브", "소바", "부타동", "나베", "오야꼬동", "에비동", "돈코츠라멘", "텐동",
        "히레카츠동", "가라아게", "오마카세", "사시미", "샤브샤브", "오꼬노미야끼", "고로케")

    when (selectedMenu) {
        "한식" -> recommendedDish = koreanDishes.random()
        "양식" -> recommendedDish = westernDishes.random()
        "중식" -> recommendedDish = chineseDishes.random()
        "일식" -> recommendedDish = japaneseDishes.random()
    }

    val buttonText = "추천 메뉴는 '$recommendedDish'입니다!"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting(buttonText, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { navController.navigate("details") }
                ) {
                    Text("뒤로")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        recommendedDish = when (selectedMenu) {
                            "한식" -> koreanDishes.random()
                            "양식" -> westernDishes.random()
                            "중식" -> chineseDishes.random()
                            "일식" -> japaneseDishes.random()
                            else -> ""
                        }
                    }
                ) {
                    Text("다시 뽑기")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { navController.navigate("home") }
                ) {
                    Text("처음으로")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, fontWeight: FontWeight = FontWeight.Normal) {
    Text(
        text = "$name",
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NewScreenActivityTheme {
//        AppNavigation()
//    }
//}