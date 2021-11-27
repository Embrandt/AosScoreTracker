package de.embrandt.aostracker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    private lateinit var viewModel: GameViewModel
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
    val titleText =
        "${viewModel.playerTotalScore} - ${viewModel.opponentTotalScore} " +
                "${viewModel.gameData.playerName} vs. ${viewModel.gameData.opponentName}"
    val turnShown by navController.currentBackStackEntryAsState()
    val currentRoute = turnShown?.destination?.route
    Scaffold(
        topBar = {
            TurnTopBar(
                titleText = titleText,
                showTurns = currentRoute == Screen.Turn.route,
                turnNumber = viewModel.currentTurn?.turnNumber ?: 0,
                onTurnChange = { viewModel.onTurnChange(it) })
        },
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
fun TurnTopBar(
    titleText: String,
    showTurns: Boolean,
    turnNumber: Int,
    onTurnChange: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        TopAppBar(title = { Text(titleText) })
        if (showTurns) {
            TopAppBar(
                modifier = Modifier.height(42.dp),
                navigationIcon = {
                    IconButton(
                        enabled = turnNumber != 1,
                        onClick = { onTurnChange(turnNumber - 1) },
                        modifier = Modifier.alpha(0.5f)
                    ) {
                        Icon(Icons.Outlined.ArrowLeft, contentDescription = null)
                    }

                },
                actions = {
                    IconButton(
                        enabled = turnNumber != 5,
                        onClick = { onTurnChange(turnNumber + 1) }) {
                        Icon(Icons.Outlined.ArrowRight, contentDescription = null)
                    }
                },
                title = {
                    var dropdownOpen by remember { mutableStateOf(false) }
                    TextButton(modifier = Modifier.weight(1f), onClick = { dropdownOpen = true }) {
                        Text(text = "Turn $turnNumber")
                        Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = dropdownOpen,
                        onDismissRequest = { dropdownOpen = false }) {
                        for (i in 1..5) {
                            DropdownMenuItem(onClick = {
                                onTurnChange(i)
                                dropdownOpen = false
                            }) {
                                Text("Turn $i")
                            }
                        }
                    }

                }
            )
            Spacer(modifier = Modifier.height(10.dp))
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

@Preview
@Composable
fun PreviewTurnTopBar() {
    AosTrackerTheme {
        TurnTopBar(titleText = "Cool Topbar", showTurns = true, turnNumber = 2, onTurnChange = {})
    }
}