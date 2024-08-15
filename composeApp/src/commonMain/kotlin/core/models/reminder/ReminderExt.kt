package core.models.reminder

import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*

fun Reminder?.getState(): Reminder.State
{
    return if (this == null)
    {
        Reminder.State.CREATE
    }
    else if (this.isAcknowledged)
    {
        Reminder.State.ACKNOWLEDGED
    }
    else
    {
        Reminder.State.UNACKNOWLEDGED
    }
}

fun Reminder.State.getIcon() = when (this)
{
    Reminder.State.CREATE -> FontAwesomeIcons.Solid.Plus
    Reminder.State.UNACKNOWLEDGED -> FontAwesomeIcons.Solid.Exclamation
    Reminder.State.ACKNOWLEDGED -> FontAwesomeIcons.Solid.Check
}

fun Reminder.getIntervalIcon() = if (this.interval.isRecurring) FontAwesomeIcons.Solid.Bell else FontAwesomeIcons.Solid.Redo