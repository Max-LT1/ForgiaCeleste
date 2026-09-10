"use strict";

import {
    inizializzaRegistrazione
} from "./log-sign"

document.addEventListener("DOMContentLoaded", () => {
    const forms = document.querySelectorAll("form");
    const modifyForm = document.getElementById("modifyForm");

    if (modifyForm) {
        inizializzaRegistrazione(modifyForm);
    }
});
