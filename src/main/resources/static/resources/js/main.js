/**
 * Main JavaScript file for Spring PetClinic
 * Initializes shadcn components and other functionality
 */

// Import component scripts
document.addEventListener('DOMContentLoaded', () => {
  // Load component scripts
  const componentScripts = [
    '/resources/js/components/ui/button.js',
    '/resources/js/components/ui/input.js',
    '/resources/js/components/ui/select.js'
  ];

  componentScripts.forEach(script => {
    const scriptElement = document.createElement('script');
    scriptElement.src = script;
    document.body.appendChild(scriptElement);
  });

  // Initialize mobile menu toggle
  const menuToggle = document.querySelector('.navbar-toggler');
  const mainNavbar = document.querySelector('#main-navbar');
  
  if (menuToggle && mainNavbar) {
    menuToggle.addEventListener('click', () => {
      const isExpanded = menuToggle.getAttribute('aria-expanded') === 'true';
      menuToggle.setAttribute('aria-expanded', !isExpanded);
      
      if (isExpanded) {
        mainNavbar.classList.add('hidden', 'md:block');
      } else {
        mainNavbar.classList.remove('hidden');
      }
    });
  }
});
