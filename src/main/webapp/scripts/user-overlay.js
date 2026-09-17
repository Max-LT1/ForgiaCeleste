

const userIcon = document.getElementById("user-icon");
const overlayU = document.getElementById("user-overlay");
const overlays = [overlayU];
const menuIcon = document.getElementById("openMenu");
const closeIcon = document.getElementById("closeMenu");
menuIcon.addEventListener("click", event =>{
    showSideBar();
});
closeIcon.addEventListener("click", event =>{
    hideSideBar();
});

function closeOverlays() {
    overlays.forEach(overlay => overlay.classList.add("hidden"));
}

function toggleOverlay(selectedOverlay) {
    const mustOpen = selectedOverlay.classList.contains("hidden");
    closeOverlays();

    if (mustOpen) {
        selectedOverlay.classList.remove("hidden");
    }
}

userIcon.addEventListener("click", event => {
    event.stopPropagation();
    toggleOverlay(overlayU);
});

overlays.forEach(overlay => {
    overlay.addEventListener("click", event => event.stopPropagation());
});

document.querySelectorAll("[data-close-overlay]").forEach(button => {
    button.addEventListener("click", closeOverlays);
});

document.addEventListener("click", closeOverlays);
document.addEventListener("keydown", event => {
    if (event.key === "Escape") {
        closeOverlays();
    }
});


function showSideBar(){
    const sidebar = document.querySelector('.side-sidebar');
    sidebar.style.display = 'flex';
}

function hideSideBar(){
    const sidebar = document.querySelector('.side-sidebar');
    sidebar.style.display = 'none';
}