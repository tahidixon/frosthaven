package ui.model

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import core.models.reminder.Reminder

class RemindersTabModel : ScreenModel {

    companion object {
        const val CREATION_MODAL_ID = "ReminderCreation#MODAL_ID"
    }

    var activeModal = mutableStateOf(null as String?)
    var reminders = mutableListOf<Reminder>()

    fun addReminder(reminder: Reminder)
    {
        reminders.add(reminder)
    }

    fun removeReminder(reminder: Reminder)
    {
        reminders.remove(reminder)
    }
}