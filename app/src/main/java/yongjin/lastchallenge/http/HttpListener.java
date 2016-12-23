package yongjin.lastchallenge.http;

/**
 * Created by jeonyongjin on 2016. 12. 22..
 */
public interface HttpListener {
    // Callback methods
    public void OnReceiveHttpResponse(int type, String strResult, int resultCode);
    public void OnReceiveFileResponse(int type, String id, String filepath, String url, int resultCode);
}
