package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class TasksViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // assunto em teste ou melhor dizendo o que vai ser testado
    private lateinit var tasksViewModel: TasksViewModel

    private lateinit var tasksRepository: FakeTestRepository


    @Before
    fun setup(){
//        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(tasksRepository)

    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // Given a fresh taskViewModel
        // a viewModel vai precisar de um contexto.
        // o Android x lib tem algumas classes e metodos que são feitos para testes.
        //
        // providencia um mock da aplicação
        // When Adding a new Task
        // o que vamos testar?
        // vamos testar que basicamente conseguimos colocar um valor nesse Live Data
        // e vamos ver como acertamos valor
        tasksViewModel.addNewTask()

        // Then the new Task event is triggered
        // robolectric simula um ambiente do android no seu emulador/
        // tem que adicionar essa lib pq o androidJunit usa ela por debaixo dos panos
        // RunWith roda um runner
        // escolhe o AndroidJunit4 pra colocar pra runner
        // se vc estiver utilizando AndroidJunit4 como runner vc pode colocar ele como local e como
        // instrumentado

        // local testes rodam na JVM
        // qual o problema do liveData?
        // Testar se o valor foi colocado dentro do liveData
        // e precisamos assetar valores
        // ou seja eu preciso verificar se no liveData tem algum valor

        // qual a solução?
        // colocar uma anotação @get:Rule
        // criar uma nova instancia dessa classe InstantTaskExecutorRule
        // o que isso faz?
        // faz com que seu liveData comece a rodar sincronamente
        // lembrando que na view temos o lifeCycleOwner nos testes não temos
        // e ele para de observar quando precisa
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled(), (not(nullValue())))

    }

    @Test
    fun addNewTask_setsNewTaskEvent2() {

        // Given a fresh ViewModel


        // Create observer - no need for it to do anything!
        // observer
        // try finally
        val observer = Observer<Event<Unit>> {}
        try {

            // Observe the LiveData forever
            tasksViewModel.newTaskEvent.observeForever(observer)

            // When adding a new task
            tasksViewModel.addNewTask()

            // Then the new task event is triggered
            val value = tasksViewModel.newTaskEvent.value
            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))

        } finally {
            // Whatever happens, don't forget to remove the observer!
            tasksViewModel.newTaskEvent.removeObserver(observer)
        }
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // verificar se o botão mostrar tarefa está visível
        // Given a fresh ViewModel
        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is` (true))
    }


   //

    // Este é um método bastante complicado.
    // Ele cria uma função de extensão Kotlin chamada getOrAwaitValue que adiciona um observador,
    // obtém o valor LiveData e, em seguida, limpa o observador — basicamente uma versão curta e reutilizável do código
    // observeForever mostrado acima. Para uma explicação completa desta classe, confira esta postagem no blog.

    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)

        try {
            afterObserve.invoke()

            // Don't wait indefinitely if the LiveData is not set.
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set.")
            }

        } finally {
            this.removeObserver(observer)
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}