package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class Turbulence extends Module {
	/// Default frequency for the noise::module::Turbulence noise module.
	public static final double DEFAULT_TURBULENCE_FREQUENCY = Perlin.DEFAULT_PERLIN_FREQUENCY;

	/// Default power for the noise::module::Turbulence noise module.
	public static final double DEFAULT_TURBULENCE_POWER = 1.0;

	/// Default roughness for the noise::module::Turbulence noise module.
	public static final int DEFAULT_TURBULENCE_ROUGHNESS = 3;

	/// Default noise seed for the noise::module::Turbulence noise module.
	public static final int DEFAULT_TURBULENCE_SEED = Perlin.DEFAULT_PERLIN_SEED;

	/// The power (scale) of the displacement.
	double power;

	/// Noise module that displaces the @a x coordinate.
	Perlin xDistortModule;

	/// Noise module that displaces the @a y coordinate.
	Perlin yDistortModule;

	/// Noise module that displaces the @a z coordinate.
	Perlin zDistortModule;

	public Turbulence() {
		super(1);
		xDistortModule = new Perlin();
		yDistortModule = new Perlin();
		zDistortModule = new Perlin();
	}

	public double getPower(){
		return power;
	}
	public void setPower(double power){
		this.power = power;
	}

	public int getRoughnessCount(){
		return xDistortModule.getOctaveCount();
	}

	public double getFrequency(){
		return xDistortModule.getFrequency();
	}

	public int getSeed(){
		return xDistortModule.getSeed();
	}

	public void setSeed(int seed){
		xDistortModule.setSeed(seed);
		yDistortModule.setSeed(seed + 1);
		zDistortModule.setSeed(seed + 2);
	}

	public void setFrequency(double frequency){
		xDistortModule.setFrequency(frequency);
		yDistortModule.setFrequency(frequency);
		zDistortModule.setFrequency(frequency);
	}
	public void setRoughness(int roughness){
		xDistortModule.setOctaveCount(roughness);
		yDistortModule.setOctaveCount(roughness);
		zDistortModule.setOctaveCount(roughness);
	}
	@Override
	public int GetSourceModuleCount() {
		return 1;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();
		
		// Get the values from the three noise::module::Perlin noise modules and
		// add each value to each coordinate of the input value.  There are also
		// some offsets added to the coordinates of the input values.  This prevents
		// the distortion modules from returning zero if the (x, y, z) coordinates,
		// when multiplied by the frequency, are near an integer boundary.  This is
		// due to a property of gradient coherent noise, which returns zero at
		// integer boundaries.
		double x0, y0, z0;
		double x1, y1, z1;
		double x2, y2, z2;
		x0 = x + (12414.0 / 65536.0);
		y0 = y + (65124.0 / 65536.0);
		z0 = z + (31337.0 / 65536.0);
		x1 = x + (26519.0 / 65536.0);
		y1 = y + (18128.0 / 65536.0);
		z1 = z + (60493.0 / 65536.0);
		x2 = x + (53820.0 / 65536.0);
		y2 = y + (11213.0 / 65536.0);
		z2 = z + (44845.0 / 65536.0);
		double xDistort = x + (xDistortModule.GetValue (x0, y0, z0)
				* power);
		double yDistort = y + (yDistortModule.GetValue (x1, y1, z1)
				* power);
		double zDistort = z + (zDistortModule.GetValue (x2, y2, z2)
				* power);

		// Retrieve the output value at the offsetted input value instead of the
		// original input value.
		return SourceModule[0].GetValue (xDistort, yDistort, zDistort);

	}

}
