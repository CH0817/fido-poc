const enc = new TextEncoder();
const dec = new TextDecoder();

function login() {
    $.ajax({
        url: '/fido/preauthenticate',
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        dataType: 'json',
        data: JSON.stringify({username: $('#username').val()}),
        success: function (data) {
            console.info(data);
            navigator.credentials.get({publicKey: convertArrayBuffer(data)})
                .then(newCredentialInfo => authenticate(newCredentialInfo))
                .catch(error => console.error(error));
        }
    })
}

function preregister() {
    $.ajax({
        url: '/fido/preregister',
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        dataType: 'json',
        data: JSON.stringify({
            username: $('#username').val(),
            displayName: $('#displayName').val()
        }),
        success: function (response) {
            console.info(response);
            if(response.code === '200') {
                navigator.credentials.create({publicKey: convertArrayBuffer(response.data)})
                    .then(newCredentialInfo => register(newCredentialInfo, response.data.user.id))
                    .catch(error => console.error(error));
            } else {
                alert(response.message);
            }
        }
    })
}

function convertArrayBuffer(publicKeyCredentialCreationOptions) {
    publicKeyCredentialCreationOptions['challenge'] = enc.encode(publicKeyCredentialCreationOptions['challenge']);
    if (publicKeyCredentialCreationOptions['user']) {
        publicKeyCredentialCreationOptions['user']['id'] = enc.encode(publicKeyCredentialCreationOptions['user']['id']);
    }
    // FIXME 不刪除這個屬性無法在 wisecure-tech.com 以外的地方測試
    if (publicKeyCredentialCreationOptions['rp']) {
        delete publicKeyCredentialCreationOptions['rp']['id'];
    }
    return publicKeyCredentialCreationOptions;
}

function register(credentialInfo, userId) {
    $.ajax({
        url: '/fido/register',
        headers: {'Content-Type': 'application/json'},
        method: 'post',
        data: JSON.stringify({
            id: credentialInfo.id,
            rawId: arrayBufferToBase64(credentialInfo.rawId),
            response: {
                clientDataJSON: arrayBufferToBase64(credentialInfo.response.clientDataJSON),
                attestationObject: arrayBufferToBase64(credentialInfo.response.attestationObject)
            },
            type: credentialInfo.type,
            username: $('#username').val(),
            userId: dec.decode(userId)
        }),
        success: data => console.info(data),
        error: error => console.error(error)
    });
}

function authenticate(credential) {
    // FIXME 還沒做
    console.info(credential);
}

function arrayBufferToBase64(arrayBuffer) {
    let binary = "";
    const bytes = new Uint8Array(arrayBuffer);
    const len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
        binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
}