package org.asue24.financetrackerfrontend.services
import khttp.post
import khttp.responses.Response
import com.fasterxml.jackson.databind.ObjectMapper
import org.asue24.financetrackerfrontend.model.dto.login.LoginRequest
import org.asue24.financetrackerfrontend.model.dto.login.LoginResponse
import org.json.JSONObject

class AuthenticationService {
fun Authenticate(request: LoginRequest,url:String):LoginResponse{
    val response: Response = post(url, json = mapOf("email" to request.email,"password" to request.password))
    val rawJson: JSONObject = response.jsonObject
    val mapper = ObjectMapper()
    return mapper.readValue(rawJson.toString(), LoginResponse::class.java)
}
}