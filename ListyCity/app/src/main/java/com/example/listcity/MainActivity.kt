package com.example.listcity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // added it myself for remember
import androidx.compose.runtime.mutableStateOf // added it myself
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue //added it myself
import androidx.compose.runtime.setValue //added it myself
import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // added it myself cause of the error when using sp for font size
import com.example.listcity.ui.theme.ListcityTheme
import androidx.compose.foundation.layout.Column // added myself
import androidx.compose.foundation.layout.Row //added myself
import androidx.compose.foundation.layout.Spacer //added it myself
import androidx.compose.foundation.lazy.LazyColumn // added it myself
import androidx.compose.foundation.lazy.items // added it myself
import androidx.compose.material3.Button //added it myself
import androidx.compose.material3.OutlinedTextField //added it myself
import androidx.compose.foundation.clickable //added it myself for deletion





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository=CityRepository()
        setContent {
            ListcityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it ) },
                        onDeleteCity = { cityRepository.deleteCity(it)},// similar to onAddCity
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
// @composable means this function describes part of the app's UI
@Composable
fun CityListScreen(
    // Cities: List<String> is the list of city names that this screen receives from
    // MainActivity
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit, // similar to AddCity
    //modifier:Modifier=Modifier allows layout information, such as padding to be passed
    // into this screen
    modifier: Modifier= Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    // ChatGPT was used to help understand how to store the selected city the user clicks on and
    // there is a need to create a null state to store  the city currently selected by the user
    var selectedCity by remember { mutableStateOf<String?>(null) } // initially nothing is selected

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = modifier.padding(all = 16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) { Text("Add City") }


            // chatGPT was used for the delete city button logic, specifically checking for a selected city
            // and calling onDeleteCity and clearing the selection after the city is deleted
            Button(
                onClick={
                    if (selectedCity != null){
                        onDeleteCity(selectedCity!!)
                        selectedCity = null
                    }
                }
            ){ Text("Delete City")}
        }
        //LazyColumn is the compose for a basic scrolling listview
        LazyColumn(modifier = modifier.fillMaxSize()) {
            //items(cities) loops through the city list and creates on UI row for each city
            items(cities) { city ->
                CityRow(city = city,
                    onClick={selectedCity=city} // every city gets its own click meaning clicking it will store that city as
                    //selected city
                )

            }
        }
    }
}
@Composable
fun CityRow(city: String,
            onClick:() -> Unit){
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable{  //ChatGPT was used to help me understadn how to make a city clickable
                onClick()
            }
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}



class CityRepository{

    private val _cities = mutableStateListOf(
        "Edmonton","Vancouver", "Moscow", "Sydney", "Berlin",
        "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"
    )

    //Get a read-only list for the UI to display
    val cities: List<String>
        get() = _cities

    fun addCity(city:String){
        _cities.add(city)
    }
    //essentially delete is the opposite of addCity so it will just remove the city from the mutable list
    fun deleteCity(city: String){
        _cities.remove(city)
    }

}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ListcityTheme {
//        Greeting("Android")
//    }
//}