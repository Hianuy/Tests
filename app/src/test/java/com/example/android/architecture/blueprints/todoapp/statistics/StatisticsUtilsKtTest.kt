package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.hamcrest.MatcherAssert.assertThat


import org.junit.Test

class StatisticsUtilsKtTest{

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero(){
        // Create a active task
        // Given
        val tasks = listOf(Task("Title","desc",isCompleted = false))

        // ele cria uma lista de tarefas
        // Call your fuction
        // when
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        // Then
        assertEquals(result.completedTasksPercent, 0f)
        assertEquals(result.activeTasksPercent, 100f)
    }

    @Test
    fun getActiveAndCompletedStats_noActive_returnsZeroHundred(){
        // queremos testar se não tem uma task ativa

        // Create a active task
        // Given
        val tasks = listOf(Task("Title","desc",isCompleted = true))

        // When
        val result = getActiveAndCompletedStats(tasks)

        // Then
        assertThat(result.completedTasksPercent, `is`(100f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
    @Test

    fun getActiveAndCompletedStats_both_returnsFortySixty() {
        // Given
        // dado 5 tasks onde duas são completas e 3 activas
        // qual a porcetagem
        val tasks = listOf(
            Task("Title","desc",isCompleted = true),
            Task("Title","desc",isCompleted = true),
            Task("Title","desc",isCompleted = true),
            Task("Title","desc",isCompleted = false),
            Task("Title","desc",isCompleted = false)
        )

        // When
        val result = getActiveAndCompletedStats(tasks)


        assertThat(result.completedTasksPercent, `is` (60f))
        assertThat(result.activeTasksPercent, `is` (40f))
    }

    @Test
    fun getActiveAndCompletedStats_error_returnsZeros(){
        // When there's an error loading stats
        val result = getActiveAndCompletedStats(null)

        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZeros() {
        // When there are no tasks
        val result = getActiveAndCompletedStats(emptyList())
        // primeira vez que eu for fazer isso vai dar erro.


        // Both active and completed tasks are 0
//        assertEquals(result.activeTasksPercent, 0f)

//        assertEquals(result.completedTasksPercent, 0f)
//        assertEquals(result.activeTasksPercent, 100f)

        // desse formato eu deixo o código melhor pra ler, lembrando que os testes também, são uma forma de
        // documentação do código
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }



    // Dado: Configure os objetos e o estado do aplicativo que você precisa para o seu teste.
    // Para este teste, o que é "dado" é que você tenha uma lista de tarefas onde a tarefa está ativa.
    //Quando: Execute a ação real no objeto que você está testando. Para este teste, significa chamar getActiveAndCompletedStats.
    //Então: É aqui que você realmente verifica o que acontece quando executa a ação em que verifica se o teste passou ou falhou.
    // Isso geralmente é um número de chamadas de função assert.
    // Para este teste, são as duas afirmações que verificam se você tem as porcentagens ativas e concluídas corretas.

    // Arrange
    // Action ação ou entrada
    // Assert -> resultado esperado

}