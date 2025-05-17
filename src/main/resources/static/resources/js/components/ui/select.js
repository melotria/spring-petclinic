/**
 * Select component based on shadcn/ui
 * This is a client-side component that can be used with data-* attributes
 */
class Select {
  constructor(element) {
    this.element = element;
    this.init();
  }

  init() {
    // Apply base styles to the select element
    this.element.classList.add(
      'flex',
      'h-10',
      'w-full',
      'rounded-md',
      'border',
      'border-input',
      'bg-background',
      'px-3',
      'py-2',
      'text-sm',
      'ring-offset-background',
      'focus-visible:outline-none',
      'focus-visible:ring-2',
      'focus-visible:ring-ring',
      'focus-visible:ring-offset-2',
      'disabled:cursor-not-allowed',
      'disabled:opacity-50'
    );

    // Add label styling if there's a label
    const label = this.element.closest('.form-group')?.querySelector('label');
    if (label) {
      label.classList.add(
        'text-sm',
        'font-medium',
        'leading-none',
        'peer-disabled:cursor-not-allowed',
        'peer-disabled:opacity-70'
      );
    }

    // Add error styling if there's an error
    const error = this.element.closest('.form-group')?.querySelector('.help-inline');
    if (error) {
      error.classList.add('text-destructive', 'text-sm', 'mt-1');
      this.element.classList.add('border-destructive');
    }

    // Style option elements
    Array.from(this.element.querySelectorAll('option')).forEach(option => {
      option.classList.add(
        'relative',
        'cursor-default',
        'select-none',
        'py-1.5',
        'pl-8',
        'pr-2',
        'text-sm',
        'outline-none',
        'data-[disabled]:pointer-events-none',
        'data-[disabled]:opacity-50'
      );
    });
  }
}

// Initialize all selects on page load
document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('[data-component="select"]').forEach(element => {
    new Select(element);
  });
});
