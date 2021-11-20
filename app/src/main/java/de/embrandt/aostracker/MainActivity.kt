package de.embrandt.aostracker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.embrandt.aostracker.data.data_source.GameDatabase
import de.embrandt.aostracker.presentation.GameViewModel
import de.embrandt.aostracker.presentation.GameViewModelFactory
import de.embrandt.aostracker.presentation.pregame.PregameScreen
import de.embrandt.aostracker.presentation.turn.TurnScreenStart
import de.embrandt.aostracker.ui.theme.AosTrackerTheme

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : GameViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val datasource = GameDatabase.getInstance(application).gameDataDao
        val gameViewModelFactory = GameViewModelFactory(datasource)
        viewModel = ViewModelProvider(this, gameViewModelFactory).get(GameViewModel::class.java)
        setContent {
            AosTrackerTheme {
                MainApp()
//                GameSelectionScreen()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel(LocalContext.current as AppCompatActivity)
    val titleText = "${viewModel.playerTotalScore} - ${viewModel.opponentTotalScore} ${viewModel.gameData.playerName} vs. ${viewModel.gameData.opponentName}"

    Scaffold(
        topBar = { TopAppBar(title = {Text(titleText)}) },
        bottomBar = {
            BottomBar(navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            BottomBarMain(navController = navController)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(Screen.Pregame, Screen.Turn)


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    BottomNavigation {
        items.map {
            BottomNavigationItem(
                icon = { Icon(painterResource(id = it.icon), contentDescription = null) },
                label = { Text(stringResource(it.resourceId)) },
                selected = currentRoute == it.route,
                onClick = { navController.navigate(it.route) }
            )
        }
    }
}

@Composable
fun BottomBarMain(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = Screen.Pregame.route
    ) {
        composable(Screen.Pregame.route) {
            PregameScreen()
        }
        composable(Screen.Turn.route) {
            TurnScreenStart()
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    object Pregame : Screen("pregame", R.string.title_pregame, R.drawable.ic_home_black_24dp)
    object Turn : Screen("turns", R.string.title_turn_display, R.drawable.ic_dashboard_black_24dp)
}

@Preview
@Composable
private fun PreviewMainApp() {
    AosTrackerTheme {
        MainApp()
    }
}