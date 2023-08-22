package com.example.newscreenactivity

import android.os.Bundle
import android.telecom.Call.Details
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newscreenactivity.ui.theme.NewScreenActivityTheme
import com.example.newscreenactivity.ui.theme.data.AppDatabase
import com.example.newscreenactivity.ui.theme.data.SelectedMenu
import com.google.android.engage.common.datamodel.Image
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewScreenActivityTheme {
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
    val navController =
        rememberNavController() // rememberNavController() 함수를 사용하여 Navigation Controller 인스턴스를 생성
    NavHost(navController = navController, startDestination = "home") {
        //NavHost는 네비게이션 그래프를 정의하고 해당 그래프에 따라 화면 간의 이동을
        composable("home") { HomeScreen(navController) }
        composable("details") { DetailsScreen(navController) }
        composable("next_destination/{selectedMenu}") { backStackEntry ->
            val selectedMenu = backStackEntry.arguments?.getString("selectedMenu") ?: ""
            //동적인 경로에서 전달된 selectedMenu 값을 가져옵니다. 해당 값이 없으면 빈 문자열을 사용
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
        Image(
            painter = painterResource(id = R.drawable.first),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
            ) {
                Text("아래의 버튼을 눌러 시작하세요!")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("details") }, //navController.navigate("details")를 호출하여 "details" 목적지로 이동
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("메뉴 추천 시작!", color = Color.Black)
            }
        }
    }
}

@Composable
fun DetailsScreen(navController: NavController) {
    var selectedMenu by remember { mutableStateOf("") } // 선택된 메뉴를 저장하는 변수로, Composable이 재생성되어도 상태가 유지됩니다.
    val context = LocalContext.current // 현재 Context를 가져옵니다. 컨텍스트를 통해 앱의 상태, 리소스 및 기능에 접근할 수 있습니다.
    val database = AppDatabase.getDatabase(context) // AppDatabase에서 데이터베이스 인스턴스를 가져옵니다.


    val selectedMenuDao = database.selectedMenuDao() // DAO를 통해 데이터베이스에서 저장된 선택된 메뉴를 가져옵니다.

    val storedSelectedMenu: SelectedMenu? = runBlocking {
        // 데이터베이스에서 저장된 선택된 메뉴를 가져오는 비동기 작업을 실행합니다.
        // runBlocking은 코루틴을 사용하여 비동기 코드를 동기적으로 실행할 수 있도록 도와줍니다.
        selectedMenuDao.getSelectedMenu()
    }
    // 가져온 선택된 메뉴가 있으면 해당 메뉴를 선택합니다. 없으면 무작위로 메뉴를 선택합니다.
    if (storedSelectedMenu != null) {
        selectedMenu = storedSelectedMenu.menu
    } else if (selectedMenu.isEmpty()) {
        val menuList = listOf("한식", "양식", "일식", "중식")
        selectedMenu = menuList.random()
    }
    // 선택된 메뉴에 따라 버튼 텍스트를 설정합니다.
    val buttonText = if (selectedMenu.isNotEmpty()) {
        "${selectedMenu}이 뽑혔습니다!"
    } else {
        "뽑혔습니다!"
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Image(
            painter = painterResource(id = R.drawable.second),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentScale = ContentScale.FillBounds
        )
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
                    onClick = { navController.navigate("home") }  // "뒤로" 버튼을 클릭하면 "home" 목적지로 이동합니다.
                ) {
                    Text("뒤로")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { //// "다시 뽑기" 버튼을 클릭하면 다른 메뉴를 무작위로 선택하고 데이터베이스에 저장합니다.
                        selectedMenu = getRandomMenuExceptSelected(selectedMenu)

                        runBlocking {
                            selectedMenuDao.insertMenu(SelectedMenu(menu = selectedMenu))
                        }
                    }
                ) {
                    Text("다시 뽑기")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        navController.navigate("next_destination/$selectedMenu") //// "다음" 버튼을 클릭하면 선택된 메뉴를 가진 화면으로 이동합니다.
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
    val remainingMenus =
        menuList.filter { it != selected } //remainingMenus는 menuList에서 selected와 같지 않은 메뉴들로 구성된 리스트입니다. 이를 위해 filter 함수를 사용하여 selected와 다른 메뉴들을 걸러냅니다.
    return remainingMenus.random() //remainingMenus.random()는 remainingMenus 리스트에서 랜덤하게 하나의 메뉴를 선택하여 반환
}

@Composable
fun NextScreen(navController: NavController, selectedMenu: String) {
    val koreanDishes = listOf(
        "불고기 덮밥",
        "된장찌개",
        "생선조림",
        "김치찌개",
        "냉면",
        "삼계탕",
        "비빔밥",
        "제육덮밥",
        "김치볶음밥",
        "닭볶음탕",
        "갈비찜",
        "장어덮밥",
        "돼지불백",
        "닭곰탕",
        "순두부찌개",
        "생선구이",
        "메밀전병",
        "돼지고기 두루치기",
        "감자탕",
        "비빔밥",
        "육개장",
        "떡만두국",
        "해물순두부찌개",
        "순대국밥",
        "보쌈",
        "족발",
        "삼겹살",
        "대패삼겹살",
        "오리주물럭",
        "해물뚝배기",
        "전복죽",
        "뚝불고기",
        "육회",
        "회",
        "회덮밥",
        "물회",
        "돼지갈비",
        "소갈비",
        "낙지볶음",
        "조개찜",
        "동태찌개",
        "낙지볶음밥",
        "곱창전골",
        "장어구이",
        "치킨",
        "김치전",
        "해물파전",
        "떡볶이",
        "순대",
        "김밥",
        "닭발",
        "고로케",
        "두부김치",
        "튀김",
        "대하구이",
        "닭꼬치",
        "호떡",
        "칼국수",
        "샤브샤브",
        "라면",
        "돈까스",
        "낙곱새",
        "소고기"
    )
    val westernDishes = listOf(
        "스테이크 샐러드",
        "치킨 샐러드",
        "파스타",
        "샌드위치",
        "그라탕",
        "햄버거",
        "오믈렛",
        "피자",
        "알리오 올리오",
        "스파게티",
        "버팔로 윙",
        "미트볼",
        "새우 스테이크",
        "포크립",
        "비프 스튜",
        "그릴드 치킨",
        "그릴드 랍스터",
        "케밥",
        "치킨 텐더",
        "감바스",
        "핫도그",
        "퀘사디아",
        "브리또",
        "카나페",
        "빵",
        "라자냐",
        "스크램블 에그",
        "오므라이스",
        "도넛"
    )
    val chineseDishes = listOf(
        "자장면",
        "볶음밥",
        "자장면",
        "볶음밥",
        "탕수육",
        "깐풍기",
        "짬뽕",
        "칠리 새우",
        "딤섬",
        "고추잡채",
        "유린기",
        "탄탄면",
        "깐쇼 새우",
        "깐쇼 치킨",
        "훠궈",
        "꿔바로우",
        "양꼬치",
        "양고기",
        "마라탕",
        "마라롱샤",
        "자장밥",
        "짬뽕밥",
        "멘보샤",
        "마파두부",
        "마파두부덮밥"
    )
    val japaneseDishes = listOf(
        "초밥",
        "덴뿌라",
        "우동",
        "라멘",
        "가츠동",
        "돈부리",
        "오니기리",
        "샤브샤브",
        "소바",
        "부타동",
        "나베",
        "오야꼬동",
        "에비동",
        "돈코츠라멘",
        "텐동",
        "히레카츠동",
        "가라아게",
        "오마카세",
        "사시미",
        "샤브샤브",
        "오꼬노미야끼",
        "고로케"
    )
    //remember 함수를 사용하여 상태를 유지하고, 상태가 변경되면 자동으로 화면이 다시 그려집니다. 초기값으로 선택된 메뉴에 따라 무작위로 요리를 선택합니다.
    var recommendedDish by remember {
        val initDish = when (selectedMenu) {
            "한식" -> koreanDishes.random()
            "양식" -> westernDishes.random()
            "중식" -> chineseDishes.random()
            "일식" -> japaneseDishes.random()
            else -> ""
        }
        mutableStateOf(initDish)
    }
    //LaunchedEffect를 사용하여 특정 이벤트가 발생할 때 동작을 수행
    LaunchedEffect(Unit) {
        repeat(20) {
            delay(50)
            recommendedDish = when (selectedMenu) {
                "한식" -> koreanDishes.random()
                "양식" -> westernDishes.random()
                "중식" -> chineseDishes.random()
                "일식" -> japaneseDishes.random()
                else -> ""
            }
        }
    }


    val buttonText = "추천 메뉴는 '$recommendedDish'입니다!"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Image(
            painter = painterResource(id = R.drawable.third),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentScale = ContentScale.FillBounds
        )
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    onClick = { navController.navigate("details") }
                ) {
                    Text("뒤로", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    onClick = {
                        GlobalScope.launch {
                            repeat(20) {
                                delay(50)
                                recommendedDish = when (selectedMenu) {
                                    "한식" -> koreanDishes.random()
                                    "양식" -> westernDishes.random()
                                    "중식" -> chineseDishes.random()
                                    "일식" -> japaneseDishes.random()
                                    else -> ""
                                }
                            }
                        }
                    }
                ) {
                    Text("다시 뽑기", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    onClick = { navController.navigate("home") }
                ) {
                    Text("처음으로", color = Color.Black)
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
//fun HomeScreenPreview() {
//    NewScreenActivityTheme {
//        HomeScreen(navController = rememberNavController())
//    }
//}