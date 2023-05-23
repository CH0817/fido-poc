const enc = new TextEncoder();

// const dec = new TextDecoder();

function login() {
    $.ajax({
        url: '/fido/preauthenticate',
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        dataType: 'json',
        data: JSON.stringify({username: $('#username').val()}),
        success: function (data) {
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
            if (response.code === '200') {
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
    // if (publicKeyCredentialCreationOptions['rp']) {
    //     delete publicKeyCredentialCreationOptions['rp']['id'];
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
            userId: uint8ArrayToString(userId)
        }),
        success: data => console.info(data),
        error: error => console.error(error)
    });
}

function authenticate(credential) {
    // FIXME 還沒做
    console.info(credential);
}

function uint8ArrayToString(array) {
    let out = "";
    let i = 0;
    let len = array.length;
    let c;
    let char2
    let char3;

    while (i < len) {
        c = array[i++];
        switch (c >> 4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                // 0xxxxxxx
                out += String.fromCharCode(c);
                break;
            case 12:
            case 13:
                // 110x xxxx   10xx xxxx
                char2 = array[i++];
                out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
                break;
            case 14:
                // 1110 xxxx  10xx xxxx  10xx xxxx
                char2 = array[i++];
                char3 = array[i++];
                out += String.fromCharCode(((c & 0x0F) << 12) |
                    ((char2 & 0x3F) << 6) |
                    ((char3 & 0x3F) << 0));
                break;
        }
    }

    return out;
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