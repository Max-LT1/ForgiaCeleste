document.addEventListener("DOMContentLoaded", function () {
    const orderHeaders = document.querySelectorAll(".order-header");
        orderHeaders.forEach(function (header) {
            header.addEventListener("click", function () {
            const detailsId = header.getAttribute("aria-controls");
            const details = document.getElementById(detailsId);
            if (!details) {
                return;
            }
            const isOpen = header.getAttribute("aria-expanded") === "true";
            header.setAttribute("aria-expanded", String(!isOpen));
            details.hidden = isOpen;
            const chevron = header.querySelector(".order-chevron");
            if (chevron) {
                chevron.textContent = isOpen ? "⌄" : "⌃";
            }
            const orderItem = header.closest(".order-item");
            if (orderItem) {
                orderItem.classList.toggle("order-item--open", !isOpen);
            }
        });
    });
});
