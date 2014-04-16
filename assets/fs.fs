precision mediump float;
varying vec2 texcoord;
uniform sampler2D sampler1;
uniform sampler2D sampler2;
void main() {
	gl_FragColor = texture2D(sampler2, texcoord);
}
