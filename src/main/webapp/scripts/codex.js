document.addEventListener("DOMContentLoaded", () => {
    const categoryButtons = [...document.querySelectorAll(".filter-button-category")];
    const typeButtons = [...document.querySelectorAll(".filter-button-type")];
    const materialButtons = [...document.querySelectorAll(".filter-button-material")];
    const products = [...document.querySelectorAll(".codex-product")];
    const resultCount = document.getElementById("result-count");
    const noResults = document.getElementById("no-filter-results");
    const grid = document.getElementById("product-grid");

    const normalize = value => (value || "")
        .trim()
        .toLocaleLowerCase("it-IT");

    let category = "all";
    let type = "all";
    let material = "all";

    function updateCount(visible) {
        if (!resultCount) return;

        resultCount.textContent =
            `${visible} ${visible === 1 ? "prodotto" : "prodotti"}`;
    }

    function applyFilter(selectedCategory, selectedType, selectedMaterial) {
        const normalizedCategoryFilter = normalize(selectedCategory);
        const normalizedTypeFilter = normalize(selectedType);
        const normalizedMaterialFilter = normalize(selectedMaterial);
        let visible = 0;

        products.forEach(product => {
            const productCategory = normalize(product.dataset.categoria);
            const productType = normalize(product.dataset.tipo);
            const productMaterial = normalize(product.dataset.materiale);

            const mustShow =
                (normalizedCategoryFilter === "all" ||
                productCategory === normalizedCategoryFilter) &&
                (normalizedTypeFilter === "all" ||
                productType === normalizedTypeFilter) &&
                (normalizedMaterialFilter === "all" ||
                productMaterial === normalizedMaterialFilter);

            product.classList.toggle("filtered-out", !mustShow);

            if (mustShow) {
                visible++;
            }
        });

        if (grid) {
            grid.classList.toggle("hidden", visible === 0);
        }

        if (noResults) {
            noResults.classList.toggle("hidden", visible !== 0);
        }

        updateCount(visible);
    }

    categoryButtons.forEach(button => {
        button.addEventListener("click", () => {
            categoryButtons.forEach(item => {
                item.classList.remove("active");
            });

            button.classList.add("active");

            category = button.dataset.filter;

            applyFilter(category, type, material);
        });
    });

    typeButtons.forEach(button => {
        button.addEventListener("click", () => {
            typeButtons.forEach(item => {
                item.classList.remove("active");
            });

            button.classList.add("active");

            type = button.dataset.filter;

            applyFilter(category, type, material);
        });
    });

    materialButtons.forEach(button => {
        button.addEventListener("click", () => {
            materialButtons.forEach(item => {
                item.classList.remove("active");
            });

            button.classList.add("active");

            material = button.dataset.filter;

            applyFilter(category, type , material);
        });
    });

    updateCount(products.length);
});