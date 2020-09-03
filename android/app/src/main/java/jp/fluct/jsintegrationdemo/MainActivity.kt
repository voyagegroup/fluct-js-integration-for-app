package jp.fluct.jsintegrationdemo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val webView by lazy { findViewById<WebView>(R.id.webView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }

        webView.getSettings().setJavaScriptEnabled(true)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                GlobalScope.launch(Dispatchers.Main) {
                    val adinfo = withContext(Dispatchers.IO) {
                        AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
                    }
                    val groupId = "1000123948"
                    val unitId = "1000214357"
                    val bundle = BuildConfig.APPLICATION_ID

                    val js = "var fluctAdScript = fluctAdScript || {};" +
                            "fluctAdScript.cmd = fluctAdScript.cmd || [];" +
                            "fluctAdScript.cmd.push(function (cmd) {" +
                            "cmd.setConfig({dlvParams: {\"ifa\":\"${adinfo.id}\",\"lmt\":${adinfo.isLimitAdTrackingEnabled},\"bundle\":\"${bundle}\"}});" +
                            "cmd.loadByGroup(\"${groupId}\");" +
                            "cmd.display(\".fluct-unit-${unitId}\", \"${unitId}\");" +
                            "});"
                    webView.evaluateJavascript(js, null)
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                startActivity(Intent(Intent.ACTION_VIEW, request?.url))
                return true
            }
        }
        webView.loadUrl("https://voyagegroup.github.io/FluctSDK-Hosting/js-sample/android.html")
    }
}
