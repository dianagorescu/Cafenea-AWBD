document.addEventListener('DOMContentLoaded', function () {
    const addItemButton = document.querySelector('#add-order-item');
    if (!addItemButton) {
        return;
    }

    addItemButton.addEventListener('click', function (event) {
        event.preventDefault();
        const itemsContainer = document.querySelector('#order-items');
        const itemIndex = itemsContainer.children.length;
        const newItem = document.createElement('div');
        newItem.className = 'item-fieldset';
        newItem.innerHTML = `
            <fieldset>
                <legend>Item ${itemIndex + 1}</legend>
                <label>
                    Menu Item
                    <select name="items[${itemIndex}].menuItemId" required>
                        <option value="" disabled selected>Select item</option>
                    </select>
                </label>
                <label>
                    Quantity
                    <input type="number" name="items[${itemIndex}].quantity" min="1" required />
                </label>
            </fieldset>
        `;

        const menuSelect = newItem.querySelector('select');
        const originalSelect = document.querySelector('[name="items[0].menuItemId"]');
        if (originalSelect) {
            originalSelect.querySelectorAll('option').forEach(option => {
                menuSelect.appendChild(option.cloneNode(true));
            });
        }

        itemsContainer.appendChild(newItem);
    });
});
