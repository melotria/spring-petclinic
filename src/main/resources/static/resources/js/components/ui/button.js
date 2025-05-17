/**
 * Button component based on shadcn/ui
 * This is a client-side component that can be used with data-* attributes
 */
class Button {
  constructor(element) {
    this.element = element;
    this.variant = element.dataset.variant || 'default';
    this.size = element.dataset.size || 'default';
    this.init();
  }

  init() {
    // Apply base styles
    this.element.classList.add(
      'inline-flex',
      'items-center',
      'justify-center',
      'whitespace-nowrap',
      'rounded-md',
      'text-sm',
      'font-medium',
      'ring-offset-background',
      'transition-colors',
      'focus-visible:outline-none',
      'focus-visible:ring-2',
      'focus-visible:ring-ring',
      'focus-visible:ring-offset-2',
      'disabled:pointer-events-none',
      'disabled:opacity-50'
    );

    // Apply variant styles
    switch (this.variant) {
      case 'default':
        this.element.classList.add('bg-primary', 'text-primary-foreground', 'hover:bg-primary/90');
        break;
      case 'destructive':
        this.element.classList.add('bg-destructive', 'text-destructive-foreground', 'hover:bg-destructive/90');
        break;
      case 'outline':
        this.element.classList.add('border', 'border-input', 'bg-background', 'hover:bg-accent', 'hover:text-accent-foreground');
        break;
      case 'secondary':
        this.element.classList.add('bg-secondary', 'text-secondary-foreground', 'hover:bg-secondary/80');
        break;
      case 'ghost':
        this.element.classList.add('hover:bg-accent', 'hover:text-accent-foreground');
        break;
      case 'link':
        this.element.classList.add('text-primary', 'underline-offset-4', 'hover:underline');
        break;
    }

    // Apply size styles
    switch (this.size) {
      case 'default':
        this.element.classList.add('h-10', 'px-4', 'py-2');
        break;
      case 'sm':
        this.element.classList.add('h-9', 'rounded-md', 'px-3');
        break;
      case 'lg':
        this.element.classList.add('h-11', 'rounded-md', 'px-8');
        break;
      case 'icon':
        this.element.classList.add('h-10', 'w-10');
        break;
    }
  }
}

// Initialize all buttons on page load
document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('[data-component="button"]').forEach(element => {
    new Button(element);
  });
});
