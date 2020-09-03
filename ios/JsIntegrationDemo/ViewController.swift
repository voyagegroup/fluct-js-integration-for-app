//
//  ViewController.swift
//  JsIntegrationDemo
//
//  Copyright © 2020 VOYAGE GROUP, INC. All rights reserved.
//

import UIKit
import WebKit
import AdSupport

class ViewController: UIViewController {
    
    @IBOutlet private weak var webView: WKWebView! {
        didSet {
            webView.navigationDelegate = self
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let request = URLRequest(url: URL(string: "https://voyagegroup.github.io/FluctSDK-Hosting/js-sample/ios.html")!)
        webView.load(request)
    }
}

extension ViewController: WKNavigationDelegate {
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        
        let groupId = "1000123947"
        let unitId = "1000214356"
        
        let js =
        """
        var fluctAdScript = fluctAdScript || {};
        fluctAdScript.cmd = fluctAdScript.cmd || [];
        fluctAdScript.cmd.push(function (cmd) {
        cmd.setConfig({dlvParams: {\"ifa\":\"\(idfa)\",\"lmt\":\(lmt),\"bundle\":\"\(bundle)\"}});
        cmd.loadByGroup(\"\(groupId)\");
        cmd.display(\".fluct-unit-\(unitId)\", \"\(unitId)\");
        });
        """
        
        webView.evaluateJavaScript(js, completionHandler: nil)
    }
}

extension ViewController {
    private var idfa: String {
        return ASIdentifierManager().advertisingIdentifier.uuidString
    }
    
    private var lmt: String {
        return ASIdentifierManager().isAdvertisingTrackingEnabled.description
    }
    private var bundle: String {
        return Bundle.main.bundleIdentifier ?? ""
    }
}
