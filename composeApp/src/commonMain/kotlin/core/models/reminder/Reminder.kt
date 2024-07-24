package core.models.reminder

class Reminder(
    val message: String,
    var isAcknowledged: Boolean,
    var isEditing: Boolean,
    val interval: ReminderInterval
) {
    enum class State
    {
        CREATE,
        UNACKNOWLEDGED,
        ACKNOWLEDGED
    }
}