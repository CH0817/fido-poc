function login() {
    $.ajax({
        url: '/fido/preauthenticate',
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        dataType: 'json',
        data: JSON.stringify({username: $('#username').val()}),
        success: function (response) {
            if (response.code === '200') {
                const options = response.data;
                const publicKeyOptions = {
                    challenge: base64UrlToArrayBuffer(options.challenge),
                    rpId: options.rpId,
                    allowCredentials: options.allowCredentials.map(c => {
                        return {
                            id: base64UrlToArrayBuffer(c.id),
                            type: c.type
                        }
                    }),
                    timeout: options.timeout,
                    userVerification: options.userVerification
                }

                const publicKey = {
                    publicKey: publicKeyOptions
                }

                navigator.credentials.get(publicKey)
                    .then(credential => authenticate(credential))
                    .catch(error => console.error(error));
            }
        }
    });
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
                const userId = response.data.user.id;
                const options = response.data;

                let publicKeyCredentialCreationOptions = {
                    rp: {
                        id: options.rp.id,
                        name: options.rp.name
                    },
                    user: {
                        id: base64UrlToArrayBuffer(options.user.id),
                        name: options.user.name,
                        displayName: options.user.displayName
                    },
                    challenge: base64UrlToArrayBuffer(options.challenge),
                    pubKeyCredParams: options.pubKeyCredParams,
                    timeout: options.timeout,
                    excludeCredentials: options.excludeCredentials.map(credential => {
                        return {
                            type: credential.type,
                            id: base64UrlToArrayBuffer(credential.id)
                        }
                    }),
                    authenticatorSelection: options.authenticatorSelection,
                    attestation: options.attestation
                };

                let credentialCreationOptions = {
                    publicKey: publicKeyCredentialCreationOptions
                };

                navigator.credentials.create(credentialCreationOptions)
                    .then(credentialInfo => register(credentialInfo, userId))
                    .catch(error => console.error(error));
            } else {
                alert(response.message);
            }
        }
    });
}

function register(credentialInfo, userId) {
    $.ajax({
        url: '/fido/register',
        headers: {'Content-Type': 'application/json'},
        method: 'post',
        data: JSON.stringify({
            id: credentialInfo.id,
            rawId: arrayBufferToBase64Url(credentialInfo.rawId),
            response: {
                clientDataJSON: arrayBufferToBase64Url(credentialInfo.response.clientDataJSON),
                attestationObject: arrayBufferToBase64Url(credentialInfo.response.attestationObject)
            },
            type: credentialInfo.type,
            username: $('#username').val(),
            userid: userId
        }),
        success: response => {
            if (response.code === '200' && response.data.status === 'ok') {
                alert('註冊成功');
            } else {
                alert(`註冊失敗，錯誤代碼：${response.data.code}，錯誤訊息：${response.data.errorMessage}`)
            }
        },
        error: error => {
            alert('發生未知錯誤，請查看 log');
            console.error(error);
        }
    });
}

function authenticate(credential) {
    $.ajax({
        url: '/fido/authenticate',
        headers: {'Content-Type': 'application/json'},
        method: 'post',
        data: JSON.stringify({
            id: credential.id,
            rawId: arrayBufferToBase64Url(credential.rawId),
            response: {
                clientDataJSON: arrayBufferToBase64Url(credential.response.clientDataJSON),
                authenticatorData: arrayBufferToBase64Url(credential.response.authenticatorData),
                signature: arrayBufferToBase64Url(credential.response.signature)
            },
            type: credential.type,
            username: $('#username').val()
        }),
        success: response => {
            if (response.code === '200' && response.data.status === 'ok') {
                alert('登入成功');
            } else {
                alert(`登入失敗，錯誤代碼：${response.data.code}，錯誤訊息：${response.data.errorMessage}`)
            }
        },
        error: error => {
            alert('發生未知錯誤，請查看 log');
            console.error(error);
        }
    });
}