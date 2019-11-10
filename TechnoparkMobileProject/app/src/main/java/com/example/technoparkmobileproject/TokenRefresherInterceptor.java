package com.example.technoparkmobileproject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


class TokenRefresherInterceptor implements Interceptor {                      //Interceptor for token refresh(in work)

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();                                   // получили запрос который вы отправили на сервер.
        Response response = chain.proceed(request);                          //тут выполняется запрос и результат в response.

        //далее вы работаете с этим response.
        //Проверяете, какой ответ вам пришел.
        //Если в нём лежит 401. значит вы формируете запрос на обновление токена,
        //выполняете его в этом же интерсепторе дальше. и снова проверяете результат.
        //если токен рефрешнулся то повторяете изначальный запрос

        return response;
    }
}
