package com.example.homework02

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homework02.ui.theme.Homework02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Homework02Theme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "sightseeing_list") {
                    composable("sightseeing_list") {
                        Surface() {
                            SightseeingList(navController)
                        }
                    }
                    composable("sight_detail/{sightName}") { backStackEntry ->
                        Surface() {
                            val sightName = backStackEntry.arguments?.getString("sightName")
                            sightName?.let {
                                val description = getDescriptionForSight(it) // 获取景点的详细介绍
                                SightDetail(it, description) {
                                    navController.navigateUp()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SightseeingList(navController: NavHostController) {
    val sights = listOf(
        "台北101" to "台北市信義區",
        "阿里山" to "嘉義縣阿里山鄉",
        "奇美博物館" to "台南市仁德區",
        "鹿港老街" to "彰化縣鹿港鎮",
        "國立故宮博物院" to "台北市士林區",
        "士林夜市" to "台北市士林區",
        "日月潭" to "南投縣魚池鄉",
        "野柳地質公園" to "新北市萬里區",
        "台南孔廟" to "台南市中西區"
    )
    LazyColumn {
        items(sights) { (name, description) ->
            SightListItem(name = name, description = description) {
                navController.navigate("sight_detail/$name")
            }
        }
    }
}


@Composable
fun SightListItem(name: String, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .clickable(onClick = onClick)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = name,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
fun SightDetail(sightName: String, description: String, navigateUp: () -> Unit) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "上一頁"
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            val imageResId = when (sightName) {
                "台北101" -> R.drawable.image1
                "阿里山" -> R.drawable.image2
                "奇美博物館" -> R.drawable.image3
                "鹿港老街" -> R.drawable.image4
                "國立故宮博物院" -> R.drawable.image5
                "士林夜市" -> R.drawable.image6
                "日月潭" -> R.drawable.image7
                "野柳地質公園" -> R.drawable.image8
                "台南孔廟" -> R.drawable.image9
                else -> R.drawable.image1
            }
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Sight Image",
                modifier = Modifier.size(200.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val intent = Intent(context, MapsActivity::class.java).apply {
                    putExtra("latitude", getLatitudeForSight(sightName))
                    putExtra("longitude", getLongitudeForSight(sightName))
                }
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "查看位置")
        }

    }
}


private fun getDescriptionForSight(sightName: String): String {
    return when (sightName) {
        "台北101" -> "台北 101 是台灣最高建築，共 101 層樓高，在這裡除了可以欣賞到壯觀的景色之外，還可以逛逛高級精品服飾店，包括 Gucci、Prada 和 Louis Vuitton。 地下樓層還有鼎泰豐。"
        "阿里山" -> "阿里山以日出、雲海、晚霞、森林、登山森林鐵路並列為阿里山五奇，聞名國際。每年三月中旬至四月中旬為櫻花季，吉野櫻、重瓣櫻、山櫻花點綴滿山，享受沐浴在森林芬多精的氛圍中，體驗身心舒暢的感受。"
        "奇美博物館" -> "奇美博物館是一座位於臺灣臺南市仁德區的博物館，為奇美實業創辦人許文龍成立，該館舍是目前臺灣館藏最豐富的私立博物館、美術館。以典藏1萬3,000件西洋藝術品為主，主展出藝術、樂器、兵器與自然史四大領域。場館由「財團法人奇美博物館基金會」負責管理與營運。"
        "鹿港老街" -> "鹿港繁盛時碼頭區的主幹─舊街，即今之埔頭、瑤林、大有三條街道，曲折的紅磚巷道兩旁林立著重新整修過的舊式店屋，閩南風味的古老宅第，門楣上的各式避邪古物，以及兩旁的門聯，都訴說著舊時父老們的生活步調及型態。"
        "國立故宮博物院" -> "作為全台最重要的博物館，裡面珍藏了許多中華文明各朝代的文物，吸引著大家前去一睹歷史課本上的時代遺跡，從展品上可以看到古人的智慧以及生活點滴，非常適合熱愛中華文化的人來參觀！故宮博物館旁邊還有一個至善園，一走進去很像是穿越到古代的江南一般，可以在那邊的涼亭靜靜地坐著， 享受寧靜美好的時光。"
        "士林夜市" -> "範圍以市定古蹟士林公有市場為中心，東至文林路、西至基河路、北至小北街與小西街的三角地帶，是台北市內最大、亦是全台打卡次數最高的夜市地標。曾多次獲選為台灣代表夜市、觀光客來台必去景點首選，揚名國際，成為台灣討論度最高之夜市。"
        "日月潭" -> "日月潭北半部形如日輪，南半部形如月鉤，故而得名，也是國內少見的活水庫。日月潭宛如圖畫山水的氤氳水氣及層次分明的山景變化，唯能乘船遊湖、親近潭水，才能完全體會日月潭之晨昏美景。"
        "野柳地質公園" -> "位於臺灣新北市萬里區野柳里的風景區，野柳地區為大屯山山脈延伸至東海之岬角，全區長約1700公尺、寬約250公尺。當地地景因造山運動、風化及海蝕等作用而呈現奇特形態的蕈狀岩，其中有「女王頭」、「仙女鞋」、「蕈狀石」、「燭臺石」、「薑石」、「單面山」等特殊景觀。"
        "台南孔廟" -> "又稱夫子廟，本是中國紀念孔子、供後人祭祀孔子的廟宇式建築，自漢武帝罷黜百家，獨尊儒術後成為藉以宣傳儒家思想的廟宇。"
        else -> ""
    }
}


private fun getLatitudeForSight(sightName: String): Double {
    return when (sightName) {
        "台北101" -> 25.03433606017522
        "阿里山" -> 23.511779216986348
        "奇美博物館" -> 22.934778155064176
        "鹿港老街" -> 24.05645773016112
        "國立故宮博物院" -> 25.102569118857073
        "士林夜市" -> 25.08818121069755
        "日月潭" -> 23.85920864402655
        "野柳地質公園" -> 25.206571907744465
        "台南孔廟" -> 22.990707734881603
        else -> 0.0
    }
}

private fun getLongitudeForSight(sightName: String): Double {
    return when (sightName) {
        "台北101" -> 121.56402120011502
        "阿里山" -> 120.80336996478503
        "奇美博物館" -> 120.22602679464488
        "鹿港老街" -> 120.43261388119437
        "國立故宮博物院" -> 121.54843885054399
        "士林夜市" -> 121.52422385239734
        "日月潭" -> 120.91630623913625
        "野柳地質公園" -> 121.69040142171187
        "台南孔廟" -> 120.20396496024895
        else -> 0.0
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Homework02Theme {
        SightseeingList(rememberNavController())
    }
}