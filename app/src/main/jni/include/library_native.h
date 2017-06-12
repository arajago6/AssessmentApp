#include <GLES2/gl2.h>

auto gVertexShader =
        "attribute vec4 vPosition;\n"
                "void main() {\n"
                "  gl_Position = vPosition;\n"
                "}\n";

auto gFragmentShader =
        "precision mediump float;\n"
                "void main() {\n"
                "  gl_FragColor = vec4(0.25, 0.32, 0.71, 1.0);\n"
                "}\n";

GLuint gProgram;
GLuint gvPositionHandle;

const GLfloat gTriangleVertices[] = { 0.0f, 0.5f, -0.5f, -0.5f,
                                      0.5f, -0.5f };

void on_surface_created();
void on_surface_changed();
void on_draw_frame();
GLuint loadShader(GLenum shaderType, const char* pSource);
GLuint createProgram(const char* pVertexSource, const char* pFragmentSource);
void renderFrame();
