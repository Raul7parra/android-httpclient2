package es.fpsumma.dam2.api.data.remote.api

import es.fpsumma.dam2.api.data.remote.dto.TareaCreateRequest
import es.fpsumma.dam2.api.data.remote.dto.TareaDTO
import es.fpsumma.dam2.api.data.remote.dto.TareaUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface TareaApi {

    @GET("api/tareas")
    suspend fun listar(): Response<List<TareaDTO>>

    @GET("api/tareas/{id}")
    suspend fun detalle(@Path("id") id: Int): Response<TareaDTO>

    @POST("api/tareas")
    suspend fun crear(@Body body: TareaCreateRequest): Response<TareaDTO>

    @PUT("api/tareas/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body body: TareaUpdateRequest): Response<TareaDTO>

    @DELETE("api/tareas/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>
}