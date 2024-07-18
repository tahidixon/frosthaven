package core.http.executor

import core.http.tasks.SyncTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ScopedTaskExecutor(val scope: CoroutineScope)
{
    fun execute(task: SyncTask)
    {
        scope.launch {
            task.request()
        }
    }
}