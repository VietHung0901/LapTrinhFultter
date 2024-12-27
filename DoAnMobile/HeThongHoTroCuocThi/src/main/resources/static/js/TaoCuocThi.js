var currentStep = 1;
var stepContainers = document.getElementsByClassName('step-container');

function nextStep(step) {
    stepContainers[currentStep - 1].style.display = 'none';
    currentStep = step;
    stepContainers[currentStep - 1].style.display = 'block';
}