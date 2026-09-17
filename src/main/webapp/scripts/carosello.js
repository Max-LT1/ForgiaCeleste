function setupCarousel(carouselId) {
    const carousel = document.getElementById(carouselId);
    const left = carousel.parentElement.querySelector(".left");
    const right = carousel.parentElement.querySelector(".right");

    let flagR = -1
    
    left.addEventListener("click", () => {
        if(carousel.scrollLeft === 0){
            carousel.scroll(carousel.scrollWidth, 0)
        }else{
            carousel.scrollLeft -= 200;
        }
    });

    right.addEventListener("click", () => {
        if(carousel.scrollLeft === flagR){
            carousel.scroll(0, 0)
        }else{
            flagR = carousel.scrollLeft;
            carousel.scrollLeft += 200;
        }
    });
}

setupCarousel("carousel1");
setupCarousel("carousel2");
