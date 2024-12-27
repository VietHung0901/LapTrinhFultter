document.addEventListener("DOMContentLoaded", function () {
    const menuTrigger = document.querySelector(".menu-trigger");
    const navMenu = document.querySelector(".main-nav ul");

    menuTrigger.addEventListener("click", function () {
        navMenu.classList.toggle("active");
    });
});
