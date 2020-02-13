package com.magic.utils.okhttp.request;

import com.magic.utils.okhttp.response.HttpResponse;

public interface HttpRequest<Req extends HttpRequest<Req>> {

    Req url(String url);

    Req queryString(String name, String value);

    Req header(String key, String value);

    Req removeHeader(String key);

    HttpResponse execute();

    //<T> void execute(Callback<T> callback);
}
