package JAVA.imagestego;

public class ImageHider {

	private ImageWriteReader imgWriteReader;
	private Steganographer steganographer;

	public ImageHider() {
		this.imgWriteReader = new ImageWriteReader();
		this.steganographer = new Steganographer();
	}

	public void hide(String canvasImageUrl, String secretImageUrl, String outputUrl) {
		this.imgWriteReader.writeImage(this.steganographer.hide(
				this.imgWriteReader.readImage(canvasImageUrl),
				this.imgWriteReader.readImage(secretImageUrl)),
				outputUrl);
		Messages.IMAGE_HIDDEN_SUCCESSFULLY.println();
	}

	public void reveal(String canvasImageUrl, String outputUrl) {
		this.imgWriteReader.writeImage(this.steganographer.reveal(
				this.imgWriteReader.readImage(canvasImageUrl)),
				outputUrl);
		Messages.IMAGE_EXTRACTED_SUCCESSFULLY.println();
	}

}
