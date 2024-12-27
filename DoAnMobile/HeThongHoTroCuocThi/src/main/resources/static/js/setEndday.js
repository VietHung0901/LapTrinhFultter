function setEndDate() {
    var startDateInput = document.getElementById("startDate");
    var endDateInput = document.getElementById("endDate");

    // Get the selected start date
    var startDate = startDateInput.value;

    // Set the end date to the same value as the start date
    endDateInput.value = startDate;
}