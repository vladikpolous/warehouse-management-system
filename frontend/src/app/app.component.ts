import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

/**
 * Main App Component for the Warehouse Management System frontend.
 * CI/CD test - this comment was added to test the CI/CD pipeline.
 */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
