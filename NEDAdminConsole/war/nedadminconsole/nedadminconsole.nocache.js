function nedadminconsole(){
  var $intern_0 = '', $intern_35 = '" for "gwt:onLoadErrorFn"', $intern_33 = '" for "gwt:onPropertyErrorFn"', $intern_21 = '"><\/script>', $intern_10 = '#', $intern_43 = '&', $intern_82 = '.cache.html', $intern_12 = '/', $intern_61 = '05076D707F97541010D49090847F4C87', $intern_63 = '0E2478812C959FF8730BD06AD6DE0FD0', $intern_65 = '0EBA18013053412C09AC07D45C47EAAE', $intern_66 = '11EAABA63C48F8C4F5AF19FA547CEFDD', $intern_67 = '19B1BB9A6BCCD25BD1FF7A56D02FA49C', $intern_68 = '268F67C8C7485E2AAB4E3F475CBC15C6', $intern_69 = '6A8E8373DD903E3092A74E41174B8636', $intern_70 = '7F2B19DD60B5FD09A98DF9C908A388BB', $intern_71 = '8BDC1C5799CC913A5E56F58A2AB8C129', $intern_72 = '955B6356E37B017D940D0001CD6ADBBC', $intern_81 = ':', $intern_27 = '::', $intern_90 = '<script defer="defer">nedadminconsole.onInjectionDone(\'nedadminconsole\')<\/script>', $intern_20 = '<script id="', $intern_30 = '=', $intern_11 = '?', $intern_73 = 'A29F7616942C811C925BE2E99BB9C3D0', $intern_74 = 'BA57655FABA5614D24857C070F8560BC', $intern_75 = 'BCE61323FCBC079945571A02F5CAE4C2', $intern_32 = 'Bad handler "', $intern_76 = 'D50691787882F24F98D4A2BF7BAFD336', $intern_89 = 'DOMContentLoaded', $intern_77 = 'E5FAEDA45E3FAC37BEDC5D27684CAC5E', $intern_78 = 'EBCB96BBFD2D9BC2CD4DF15133756D5B', $intern_79 = 'F621937F1FA4C3D1E774F7E59ABA2157', $intern_80 = 'F6C305E517BC8B25A9C6046F5A6032FC', $intern_22 = 'SCRIPT', $intern_46 = 'Unexpected exception in locale detection, using default: ', $intern_45 = '_', $intern_44 = '__gwt_Locale', $intern_19 = '__gwt_marker_nedadminconsole', $intern_23 = 'base', $intern_15 = 'baseUrl', $intern_4 = 'begin', $intern_3 = 'bootstrap', $intern_14 = 'clear.cache.gif', $intern_29 = 'content', $intern_64 = 'de', $intern_62 = 'default', $intern_41 = 'en', $intern_9 = 'end', $intern_55 = 'gecko', $intern_56 = 'gecko1_8', $intern_5 = 'gwt.codesvr=', $intern_6 = 'gwt.hosted=', $intern_7 = 'gwt.hybrid', $intern_83 = 'gwt/chrome/chrome.css', $intern_34 = 'gwt:onLoadErrorFn', $intern_31 = 'gwt:onPropertyErrorFn', $intern_28 = 'gwt:property', $intern_88 = 'head', $intern_59 = 'hosted.html?nedadminconsole', $intern_87 = 'href', $intern_54 = 'ie6', $intern_53 = 'ie8', $intern_52 = 'ie9', $intern_36 = 'iframe', $intern_13 = 'img', $intern_37 = "javascript:''", $intern_84 = 'link', $intern_58 = 'loadExternalRefs', $intern_40 = 'locale', $intern_42 = 'locale=', $intern_24 = 'meta', $intern_39 = 'moduleRequested', $intern_8 = 'moduleStartup', $intern_51 = 'msie', $intern_25 = 'name', $intern_1 = 'nedadminconsole', $intern_17 = 'nedadminconsole.nocache.js', $intern_26 = 'nedadminconsole::', $intern_48 = 'opera', $intern_38 = 'position:absolute;width:0;height:0;border:none', $intern_85 = 'rel', $intern_50 = 'safari', $intern_16 = 'script', $intern_60 = 'selectingPermutation', $intern_2 = 'startup', $intern_86 = 'stylesheet', $intern_18 = 'undefined', $intern_57 = 'unknown', $intern_47 = 'user.agent', $intern_49 = 'webkit';
  var $wnd = window, $doc = document, $stats = $wnd.__gwtStatsEvent?function(a){
    return $wnd.__gwtStatsEvent(a);
  }
  :null, $sessionId = $wnd.__gwtStatsSessionId?$wnd.__gwtStatsSessionId:null, scriptsDone, loadDone, bodyDone, base = $intern_0, metaProps = {}, values = [], providers = [], answers = [], softPermutationId = 0, onLoadErrorFunc, propertyErrorFunc;
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_4});
  if (!$wnd.__gwt_stylesLoaded) {
    $wnd.__gwt_stylesLoaded = {};
  }
  if (!$wnd.__gwt_scriptsLoaded) {
    $wnd.__gwt_scriptsLoaded = {};
  }
  function isHostedMode(){
    var result = false;
    try {
      var query = $wnd.location.search;
      return (query.indexOf($intern_5) != -1 || (query.indexOf($intern_6) != -1 || $wnd.external && $wnd.external.gwtOnLoad)) && query.indexOf($intern_7) == -1;
    }
     catch (e) {
    }
    isHostedMode = function(){
      return result;
    }
    ;
    return result;
  }

  function maybeStartModule(){
    if (scriptsDone && loadDone) {
      var iframe = $doc.getElementById($intern_1);
      var frameWnd = iframe.contentWindow;
      if (isHostedMode()) {
        frameWnd.__gwt_getProperty = function(name){
          return computePropValue(name);
        }
        ;
      }
      nedadminconsole = null;
      frameWnd.gwtOnLoad(onLoadErrorFunc, $intern_1, base, softPermutationId);
      $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_8, millis:(new Date).getTime(), type:$intern_9});
    }
  }

  function computeScriptBase(){
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf($intern_10);
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf($intern_11);
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf($intern_12, Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):$intern_0;
    }

    function ensureAbsoluteUrl(url){
      if (url.match(/^\w+:\/\//)) {
      }
       else {
        var img = $doc.createElement($intern_13);
        img.src = url + $intern_14;
        url = getDirectoryOfFile(img.src);
      }
      return url;
    }

    function tryMetaTag(){
      var metaVal = __gwt_getMetaProperty($intern_15);
      if (metaVal != null) {
        return metaVal;
      }
      return $intern_0;
    }

    function tryNocacheJsTag(){
      var scriptTags = $doc.getElementsByTagName($intern_16);
      for (var i = 0; i < scriptTags.length; ++i) {
        if (scriptTags[i].src.indexOf($intern_17) != -1) {
          return getDirectoryOfFile(scriptTags[i].src);
        }
      }
      return $intern_0;
    }

    function tryMarkerScript(){
      var thisScript;
      if (typeof isBodyLoaded == $intern_18 || !isBodyLoaded()) {
        var markerId = $intern_19;
        var markerScript;
        $doc.write($intern_20 + markerId + $intern_21);
        markerScript = $doc.getElementById(markerId);
        thisScript = markerScript && markerScript.previousSibling;
        while (thisScript && thisScript.tagName != $intern_22) {
          thisScript = thisScript.previousSibling;
        }
        if (markerScript) {
          markerScript.parentNode.removeChild(markerScript);
        }
        if (thisScript && thisScript.src) {
          return getDirectoryOfFile(thisScript.src);
        }
      }
      return $intern_0;
    }

    function tryBaseTag(){
      var baseElements = $doc.getElementsByTagName($intern_23);
      if (baseElements.length > 0) {
        return baseElements[baseElements.length - 1].href;
      }
      return $intern_0;
    }

    var tempBase = tryMetaTag();
    if (tempBase == $intern_0) {
      tempBase = tryNocacheJsTag();
    }
    if (tempBase == $intern_0) {
      tempBase = tryMarkerScript();
    }
    if (tempBase == $intern_0) {
      tempBase = tryBaseTag();
    }
    if (tempBase == $intern_0) {
      tempBase = getDirectoryOfFile($doc.location.href);
    }
    tempBase = ensureAbsoluteUrl(tempBase);
    base = tempBase;
    return tempBase;
  }

  function processMetas(){
    var metas = document.getElementsByTagName($intern_24);
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name = meta.getAttribute($intern_25), content;
      if (name) {
        name = name.replace($intern_26, $intern_0);
        if (name.indexOf($intern_27) >= 0) {
          continue;
        }
        if (name == $intern_28) {
          content = meta.getAttribute($intern_29);
          if (content) {
            var value, eq = content.indexOf($intern_30);
            if (eq >= 0) {
              name = content.substring(0, eq);
              value = content.substring(eq + 1);
            }
             else {
              name = content;
              value = $intern_0;
            }
            metaProps[name] = value;
          }
        }
         else if (name == $intern_31) {
          content = meta.getAttribute($intern_29);
          if (content) {
            try {
              propertyErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_32 + content + $intern_33);
            }
          }
        }
         else if (name == $intern_34) {
          content = meta.getAttribute($intern_29);
          if (content) {
            try {
              onLoadErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_32 + content + $intern_35);
            }
          }
        }
      }
    }
  }

  function __gwt_isKnownPropertyValue(propName, propValue){
    return propValue in values[propName];
  }

  function __gwt_getMetaProperty(name){
    var value = metaProps[name];
    return value == null?null:value;
  }

  function unflattenKeylistIntoAnswers(propValArray, value){
    var answer = answers;
    for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
      answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
    }
    answer[propValArray[n]] = value;
  }

  function computePropValue(propName){
    var value = providers[propName](), allowedValuesMap = values[propName];
    if (value in allowedValuesMap) {
      return value;
    }
    var allowedValuesList = [];
    for (var k in allowedValuesMap) {
      allowedValuesList[allowedValuesMap[k]] = k;
    }
    if (propertyErrorFunc) {
      propertyErrorFunc(propName, allowedValuesList, value);
    }
    throw null;
  }

  var frameInjected;
  function maybeInjectFrame(){
    if (!frameInjected) {
      frameInjected = true;
      var iframe = $doc.createElement($intern_36);
      iframe.src = $intern_37;
      iframe.id = $intern_1;
      iframe.style.cssText = $intern_38;
      iframe.tabIndex = -1;
      $doc.body.appendChild(iframe);
      $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_8, millis:(new Date).getTime(), type:$intern_39});
      iframe.contentWindow.location.replace(base + initialHtml);
    }
  }

  providers[$intern_40] = function(){
    var locale = null;
    var rtlocale = $intern_41;
    try {
      if (!locale) {
        var queryParam = location.search;
        var qpStart = queryParam.indexOf($intern_42);
        if (qpStart >= 0) {
          var value = queryParam.substring(qpStart + 7);
          var end = queryParam.indexOf($intern_43, qpStart);
          if (end < 0) {
            end = queryParam.length;
          }
          locale = queryParam.substring(qpStart + 7, end);
        }
      }
      if (!locale) {
        locale = __gwt_getMetaProperty($intern_40);
      }
      if (!locale) {
        locale = $wnd[$intern_44];
      }
      if (locale) {
        rtlocale = locale;
      }
      while (locale && !__gwt_isKnownPropertyValue($intern_40, locale)) {
        var lastIndex = locale.lastIndexOf($intern_45);
        if (lastIndex < 0) {
          locale = null;
          break;
        }
        locale = locale.substring(0, lastIndex);
      }
    }
     catch (e) {
      alert($intern_46 + e);
    }
    $wnd[$intern_44] = rtlocale;
    return locale || $intern_41;
  }
  ;
  values[$intern_40] = {de:0, 'default':1, en:2};
  providers[$intern_47] = function(){
    var ua = navigator.userAgent.toLowerCase();
    var makeVersion = function(result){
      return parseInt(result[1]) * 1000 + parseInt(result[2]);
    }
    ;
    if (function(){
      return ua.indexOf($intern_48) != -1;
    }
    ())
      return $intern_48;
    if (function(){
      return ua.indexOf($intern_49) != -1;
    }
    ())
      return $intern_50;
    if (function(){
      return ua.indexOf($intern_51) != -1 && $doc.documentMode >= 9;
    }
    ())
      return $intern_52;
    if (function(){
      return ua.indexOf($intern_51) != -1 && $doc.documentMode >= 8;
    }
    ())
      return $intern_53;
    if (function(){
      var result = /msie ([0-9]+)\.([0-9]+)/.exec(ua);
      if (result && result.length == 3)
        return makeVersion(result) >= 6000;
    }
    ())
      return $intern_54;
    if (function(){
      return ua.indexOf($intern_55) != -1;
    }
    ())
      return $intern_56;
    return $intern_57;
  }
  ;
  values[$intern_47] = {gecko1_8:0, ie6:1, ie8:2, ie9:3, opera:4, safari:5};
  nedadminconsole.onScriptLoad = function(){
    if (frameInjected) {
      loadDone = true;
      maybeStartModule();
    }
  }
  ;
  nedadminconsole.onInjectionDone = function(){
    scriptsDone = true;
    $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_58, millis:(new Date).getTime(), type:$intern_9});
    maybeStartModule();
  }
  ;
  processMetas();
  computeScriptBase();
  var strongName;
  var initialHtml;
  if (isHostedMode()) {
    if ($wnd.external && ($wnd.external.initModule && $wnd.external.initModule($intern_1))) {
      $wnd.location.reload();
      return;
    }
    initialHtml = $intern_59;
    strongName = $intern_0;
  }
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_60});
  if (!isHostedMode()) {
    try {
      unflattenKeylistIntoAnswers([$intern_41, $intern_54], $intern_61);
      unflattenKeylistIntoAnswers([$intern_62, $intern_50], $intern_63);
      unflattenKeylistIntoAnswers([$intern_64, $intern_50], $intern_65);
      unflattenKeylistIntoAnswers([$intern_41, $intern_48], $intern_66);
      unflattenKeylistIntoAnswers([$intern_62, $intern_53], $intern_67);
      unflattenKeylistIntoAnswers([$intern_64, $intern_52], $intern_68);
      unflattenKeylistIntoAnswers([$intern_64, $intern_48], $intern_69);
      unflattenKeylistIntoAnswers([$intern_64, $intern_56], $intern_70);
      unflattenKeylistIntoAnswers([$intern_41, $intern_52], $intern_71);
      unflattenKeylistIntoAnswers([$intern_64, $intern_54], $intern_72);
      unflattenKeylistIntoAnswers([$intern_62, $intern_56], $intern_73);
      unflattenKeylistIntoAnswers([$intern_41, $intern_53], $intern_74);
      unflattenKeylistIntoAnswers([$intern_64, $intern_53], $intern_75);
      unflattenKeylistIntoAnswers([$intern_62, $intern_48], $intern_76);
      unflattenKeylistIntoAnswers([$intern_41, $intern_50], $intern_77);
      unflattenKeylistIntoAnswers([$intern_62, $intern_52], $intern_78);
      unflattenKeylistIntoAnswers([$intern_62, $intern_54], $intern_79);
      unflattenKeylistIntoAnswers([$intern_41, $intern_56], $intern_80);
      strongName = answers[computePropValue($intern_40)][computePropValue($intern_47)];
      var idx = strongName.indexOf($intern_81);
      if (idx != -1) {
        softPermutationId = Number(strongName.substring(idx + 1));
        strongName = strongName.substring(0, idx);
      }
      initialHtml = strongName + $intern_82;
    }
     catch (e) {
      return;
    }
  }
  var onBodyDoneTimerId;
  function onBodyDone(){
    if (!bodyDone) {
      bodyDone = true;
      if (!__gwt_stylesLoaded[$intern_83]) {
        var l = $doc.createElement($intern_84);
        __gwt_stylesLoaded[$intern_83] = l;
        l.setAttribute($intern_85, $intern_86);
        l.setAttribute($intern_87, base + $intern_83);
        $doc.getElementsByTagName($intern_88)[0].appendChild(l);
      }
      maybeStartModule();
      if ($doc.removeEventListener) {
        $doc.removeEventListener($intern_89, onBodyDone, false);
      }
      if (onBodyDoneTimerId) {
        clearInterval(onBodyDoneTimerId);
      }
    }
  }

  if ($doc.addEventListener) {
    $doc.addEventListener($intern_89, function(){
      maybeInjectFrame();
      onBodyDone();
    }
    , false);
  }
  var onBodyDoneTimerId = setInterval(function(){
    if (/loaded|complete/.test($doc.readyState)) {
      maybeInjectFrame();
      onBodyDone();
    }
  }
  , 50);
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_9});
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_58, millis:(new Date).getTime(), type:$intern_4});
  $doc.write($intern_90);
}

nedadminconsole();
