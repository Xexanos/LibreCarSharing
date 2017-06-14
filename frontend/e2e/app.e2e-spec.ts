import { LibreCarSharingPage } from './app.po';

describe('libre-car-sharing App', () => {
  let page: LibreCarSharingPage;

  beforeEach(() => {
    page = new LibreCarSharingPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
