import java.util.ArrayList;

/*
  Emily Meuer
 06/07/2016
 
 ArrayList wrapper class to enable easy dealings with multiple inputs.
 */

class MultipleInputs
{
  ArrayList<InputPitch>  multipleInputs;

  /**
   * Constructor for creating an empty MultipleInputs object.
   */
  MultipleInputs()
  {
    this.multipleInputs = new ArrayList<InputPitch>();
  } // constructor

  /**
   * Constructor for creating a new MultipleInputs object from an array of Strings
   * that correspond to names of audio files to be made into InputPitch objects.
   */
  MultipleInputs(String[] inputNames)
  {
    if (inputNames == null) {  
      throw new IllegalArgumentException("MultipleInputsClass.constructor(String[]): String[] parameter " + inputNames + " is null.");
    }
    this.multipleInputs  = new ArrayList<InputPitch>();

    for (int i = 0; i < inputNames.length; i++)
    {
      this.multipleInputs.add(new InputPitch(inputNames[i]));
    } // for
  } // constructor(String[])

  /**
   * Constructor for creating a new MultipleInputs object from an array of InputPitch objects; 
   * simply fills the ArrayList instance var w/the given InputPitch objects.
   */
  MultipleInputs(InputPitch[] inputs)
  {
    if (inputs == null) {  
      throw new IllegalArgumentException("MultipleInputsClass.constructor(InputPitch[]): InputPitch[] parameter " + inputs + " is null.");
    }

    this.multipleInputs  = new ArrayList<InputPitch>();

    for (int i = 0; i < inputs.length; i++)
    {
      this.multipleInputs.add(inputs[i]);
    } // for
  } // constructor(String[])

  /**
   * Constructor for creating a new MultipleInputs object from one audio file.
   *
   * @param  inputName  String corresponding to the name of an audio file.
   */
  MultipleInputs(String inputName)
  {
    this.multipleInputs  = new ArrayList<InputPitch>();

    this.multipleInputs.add(new InputPitch(inputName));
  } // constructor(String[])

  /**
   * Constructor for creating a new MultipleInputs object from one InputPitch object.
   *
   * @param  input  InputPitch object to be added to the ArrayList instance var.
   */
  MultipleInputs(InputPitch input)
  {
    this.multipleInputs  = new ArrayList<InputPitch>();

    this.multipleInputs.add(input);
  } // constructor(String[])

  InputPitch get(int i)
  {
    return this.multipleInputs.get(i);
  } // get

  void add(String filename)
  {
    this.multipleInputs.add(new InputPitch(filename));
  } // add

  void add(InputPitch inputPitch)
  {
    this.multipleInputs.add(inputPitch);
  } // add

  int size()
  {
    return this.multipleInputs.size();
  } // size
} // MultipleInputs