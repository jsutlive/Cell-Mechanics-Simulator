package Utilities.Geometry.Vector;

public interface IVector {

    IVector add(IVector vec);
    IVector sub(IVector vec);
    IVector mul(float f);
    IVector div(float f);
    
    IVector dot(IVector vec);
    IVector cross(IVector vec);

    IVector unit();
    IVector dist();
    IVector mag();

    boolean equals();
    boolean isNull();

    float[] get();
}
