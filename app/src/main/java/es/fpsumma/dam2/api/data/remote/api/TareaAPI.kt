package es.fpsumma.dam2.api.data.remote.api

import es.fpsumma.dam2.api.data.remote.dto.TareaCreateRequestDTO
import es.fpsumma.dam2.api.data.remote.dto.TareaDTO
import es.fpsumma.dam2.api.data.remote.dto.TareaUpdateRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TareaAPI {

    @GET("api/tareas")
    suspend fun listar(): Response<List<TareaDTO>> //Cuando pongamos la URL su respuesta será un List<TareaDTO>

    @DELETE("api/tareas/{id}")
    suspend fun eliminar(@Path ("id") id: Int): Response<Unit> //Cuando pongamos la URL eliminará la tarea

    @POST("api/tareas")
    suspend fun crear(@Body body: TareaCreateRequestDTO) :Response<TareaDTO> //Cuando pongamos la URL creará una tarea

    @PUT("api/tareas/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body body: TareaUpdateRequestDTO): Response<TareaDTO> //Cuando pongamos la URL actualizará la tarea con ese ID

    @GET ("api/tareas/{id}")
    suspend fun listarid(@Path ("id") id: Int): Response<TareaDTO> //Cuando pongamos la URL listará la tarea que tenga ese ID
}