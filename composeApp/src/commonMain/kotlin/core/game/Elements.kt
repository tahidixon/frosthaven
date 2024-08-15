package core.game

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*

enum class Elements(val iconResource: ImageVector, val label: String) {
    FIRE(FontAwesomeIcons.Solid.Fire, "Fire"),
    FROST(FontAwesomeIcons.Solid.Snowflake, "Frost"),
    WIND(FontAwesomeIcons.Solid.Wind, "Wind"),
    PLANT(FontAwesomeIcons.Solid.Leaf, "Leaf"),
    SUN(FontAwesomeIcons.Solid.Sun, "Sun"),
    DARK(FontAwesomeIcons.Solid.Moon, "Dark")
}