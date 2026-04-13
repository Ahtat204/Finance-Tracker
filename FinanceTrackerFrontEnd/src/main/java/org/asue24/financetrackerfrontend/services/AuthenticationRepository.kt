package org.asue24.financetrackerfrontend.services
import khttp.post
import khttp.responses.Response
import com.fasterxml.jackson.databind.ObjectMapper
import org.asue24.financetrackerfrontend.model.dto.LoginRequest
import org.asue24.financetrackerfrontend.model.dto.LoginResponse
import org.asue24.financetrackerfrontend.model.dto.RegisterReponse
import org.asue24.financetrackerfrontend.model.dto.RegisterRequest
import org.json.JSONObject

class AuthenticationRepository {
fun login(request: LoginRequest, url:String):LoginResponse{
    val response: Response = post(url, json = mapOf("email" to request.email,"password" to request.password))
    val rawJson: JSONObject = response.jsonObject
    val mapper = ObjectMapper()
    return mapper.readValue(rawJson.toString(), LoginResponse::class.java)
}
    fun register(request: RegisterRequest,url:String):RegisterReponse{
        val response: Response = post(url, json = mapOf("email" to request.email,"password" to request.password))
        val rawJson: JSONObject = response.jsonObject
        val mapper = ObjectMapper()
        return mapper.readValue(rawJson.toString(), RegisterReponse::class.java)
    }
}