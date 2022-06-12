package com.cezila.essential.presentation.home_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cezila.essential.R
import com.cezila.essential.presentation.DifficultyIcons
import com.cezila.essential.presentation.bottom_nav.BottomNavItem
import com.cezila.essential.presentation.bottom_nav.BottomNavigationBar
import com.cezila.essential.presentation.destinations.DrinkScreenDestination
import com.cezila.essential.presentation.destinations.SearchScreenDestination
import com.cezila.essential.ui.theme.*
import com.cezila.essential.util.Commom.route
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
@Destination("home")
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )
    val state = viewModel.state
    val gson = Gson()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(name = "Discover", route = "home", icon = R.drawable.flame),
                    BottomNavItem(name = "Search", route = "search", R.drawable.searchicons),
                    BottomNavItem(name = "About", route = "about", R.drawable.about_icon)
                ),
                onItemClick = {
                    route = it.route
                    navigator.navigate(route = route)
                }
            )
        }
    ) {
        if (!state.isLoading) {
            val featuredRecipe = state.drinks.last()
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(HomeEvent.Refresh)
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundColor)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 58.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clickable {
                                val drinkSerializable = gson.toJson(featuredRecipe)
                                navigator.navigate(DrinkScreenDestination(drinkSerializable))
                            }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(featuredRecipe.image)
                                .build(),
                            contentDescription = null,
                            alignment = Alignment.Center,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .height(1027.dp)
                                .width(680.dp),
                            colorFilter = ColorFilter.tint(Film, BlendMode.Multiply)
                        )

                        Column(
                            modifier = Modifier
                                .padding(start = 15.dp)
                        ) {
                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Featured Recipe",
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = featuredRecipe.name,
                                fontFamily = Nuosu,
                                fontSize = 40.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .width(200.dp)
                            )

                            DifficultyIcons(
                                drink = featuredRecipe,
                                modifier = Modifier.width(60.dp)
                            )
                        }

                        // rounded icon com fotinha
                        Text(
                            text = featuredRecipe.author,
                            fontFamily = Nuosu,
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 30.dp, bottom = 20.dp)
                                .align(Alignment.BottomStart)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 25.dp, top = 25.dp)
                    ) {
                        Text(
                            text = "Today Recipes",
                            fontFamily = Nuosu,
                            color = Color.White,
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.width(140.dp))

                        Text(
                            text = "View All",
                            fontFamily = Nuosu,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .clickable {
                                    route = "search"
                                    navigator.navigate(SearchScreenDestination)
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxHeight()
                            .padding(start = 10.dp, end = 5.dp)
                    ) {
                        val todayRecipes =
                            if (state.drinks.isNotEmpty() && state.drinks.size >= 3) 3 else 0
                        items(todayRecipes) { i ->
                            val drink = state.drinks[i]
                            TodayDrinkItem(
                                drink = drink,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(250.dp)
                                    .clickable {
                                        val drinkSerializable = gson.toJson(drink)
                                        navigator.navigate(DrinkScreenDestination(drinkSerializable))
                                    }
                                    .padding(start = 8.dp, bottom = 8.dp, end = 8.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp,
                                            bottomStart = 10.dp,
                                            bottomEnd = 10.dp
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = YellowSoft)
            }
        }
    }

}