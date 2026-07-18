function setupCarousel(carouselId) {
    const carousel = document.getElementById(carouselId);
    const left = carousel.parentElement.querySelector(".left");
    const right = carousel.parentElement.querySelector(".right");

    left.addEventListener("click", () => {
        carousel.scrollLeft -= 400;
    });

    right.addEventListener("click", () => {
        carousel.scrollLeft += 400;
    });
}

setupCarousel("carousel1");
setupCarousel("carousel2");
setupCarousel("carousel3");