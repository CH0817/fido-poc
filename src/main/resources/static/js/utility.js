function base64ToBase64Url(base64){
	var base64Url = base64.replace(/\+/g,'-').replace(/\//g,'_').replace(/\=+$/m,'');
	return base64Url;
}


function base64UrlToBase64(base64Url){
	var needPadLen = (4 - (base64Url.length % 4)) %4;
	var base64 = base64Url.replace(/\-/g,'+').replace(/\_/g,'/');
	for(var i = 0; i < needPadLen; i++)
		base64 = base64.concat('=');
	return base64;
}


function base64UrlToArrayBuffer(base64Url) {
	var base64 = base64UrlToBase64(base64Url);
    var binary_string =  window.atob(base64);
    var len = binary_string.length;	
    var bytes = new Uint8Array(len);
    for (var i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);    
	}	
	return bytes;	
}


function arrayBufferToBase64(arrayBuffer) {
	var str = String.fromCharCode.apply(null, new Uint8Array(arrayBuffer));
	var b64encoded = btoa(str)	
	return b64encoded;
}


function arrayBufferToBase64Url(arrayBuffer) {
	var str = String.fromCharCode.apply(null, new Uint8Array(arrayBuffer));
	var b64encoded = btoa(str)	
	var b64url = base64ToBase64Url(b64encoded);
	return b64url;
}


function ab2str(buf) {
  return String.fromCharCode.apply(null, new Uint16Array(buf));
}


function str2ab(str) {
  var buf = new ArrayBuffer(str.length*2); // 2 bytes for each char
  var bufView = new Uint16Array(buf);
  for (var i=0, strLen=str.length; i < strLen; i++) {
    bufView[i] = str.charCodeAt(i);
  }
  return buf;
}


function buf2hex(buffer) { // buffer is an ArrayBuffer
  return Array.prototype.map.call(new Uint8Array(buffer), x => ('00' + x.toString(16)).slice(-2)).join('');
}
