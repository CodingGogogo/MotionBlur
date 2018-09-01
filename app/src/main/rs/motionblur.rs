#pragma version(1)
#pragma rs java_package_name(com.example.lingye.motionblurdemo)

uint32_t dx;
uint32_t weight;
uint32_t delta;

rs_allocation allocation_out;
rs_allocation allocation_in;

void init_var(uint32_t d, uint32_t w, uint32_t s){
    dx = d;
    weight = w;
    if(dx > 0) delta = s;
    else delta = -1 * s;
}

void RS_KERNEL motionblur(uchar4 in, uint32_t x, uint32_t y) {
    uint32_t index = 0, count = 0;
    float red = 0, green = 0, blue = 0, alpha = 0;
    for(uint32_t i = 0; i < dx; i++) {
        index = x + delta * i;
        if(index >= 0 && index < weight) {
            count++;
            uchar4 color = rsGetElementAt_uchar4(allocation_in, index, y);

            red += color.r;
            green += color.g;
            blue += color.b;
            alpha += color.a;
        }
    }

    if(count != 0) {
        red /= count;
        green /= count;
        blue /= count;
        alpha /= count;
    }

    uchar4 out_color = in;
    out_color.r = red;
    out_color.g = green;
    out_color.b = blue;
    out_color.a = alpha;
    rsSetElementAt_uchar4(allocation_out, out_color, x, y);
}