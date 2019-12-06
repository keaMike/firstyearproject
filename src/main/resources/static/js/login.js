//Selects all html lines with 'input' as class
const inputs = document.querySelectorAll('.input');

function focusFunc() {
    let parent = this.parentNode.parentNode;
    parent.classList.add('focus');
}

function blurFunc() {
    let parent = this.parentNode.parentNode;
    if(this.value == "") {
        parent.classList.remove('focus');
    }
}

//For each 'input' add event listener, if clicked add 'focus' to class, which triggers CSS, that performs the animation.
//if clicked outside input, add 'blur', which removes 'focus' from class and relation to CSS.
inputs.forEach(input => {
    input.addEventListener('focus', focusFunc);
    input.addEventListener('blur', blurFunc);
});

//CONFIRM PASSWORD
//Select element with id = 'password' and 'confirmpassword' and save them as an variable
var password = document.getElementById("password")
    , confirm_password = document.getElementById("confirmpassword");

//Checks if both passwords are identical
function validatePassword(){
    if(password.value != confirm_password.value) {
        confirm_password.setCustomValidity("Kodeordene er ikke ens");
    } else {
        confirm_password.setCustomValidity('');
    }
}

//When password changes and confirm password letter is pressed, check if identical
password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;