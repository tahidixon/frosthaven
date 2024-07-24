package ui.screen.constants

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bell
import compose.icons.fontawesomeicons.solid.DiceD20
import compose.icons.fontawesomeicons.solid.DiceOne

enum class Screens(
    val title: String,
    val route: String,
    val icon: ImageVector,
    val badgeCount: Int? = null)
{
   TURN(
        title = "Turn",
        route = "turn",
        icon = FontAwesomeIcons.Solid.DiceD20
    ),
    REMINDERS(
        title = "Reminders",
        route = "reminders",
        icon = FontAwesomeIcons.Solid.Bell
    )
}